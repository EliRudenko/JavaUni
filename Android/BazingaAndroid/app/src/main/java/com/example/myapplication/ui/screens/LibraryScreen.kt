package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.LibraryItemDto
import com.example.myapplication.data.resolveImageUrl
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.components.ErrorState
import com.example.myapplication.ui.components.LoadingState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    repository: BazingaRepository,
    authState: AuthState,
    onAuthStateChange: (AuthState) -> Unit,
    onReadComic: (Long) -> Unit
) {
    var libraryState by remember { mutableStateOf<UiState<List<LibraryItemDto>>>(UiState.Loading) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var isRegister by rememberSaveable { mutableStateOf(false) }
    var authMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState.token) {
        if (authState.token.isNotBlank()) {
            libraryState = repository.fetchLibrary(authState.token)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your Library",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (authState.token.isBlank()) {
                Text(
                    text = "Sign in to view your digital collection.",
                    color = BazingaTextMuted
                )
                if (isRegister) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        authMessage = null
                        if (isRegister) {
                            if (username.isBlank()) {
                                authMessage = "Please enter a username."
                            } else {
                                scope.launch {
                                    when (val result = repository.register(username.trim(), email.trim(), password)) {
                                        is UiState.Success -> {
                                            onAuthStateChange(
                                                AuthState(
                                                    token = result.data.token,
                                                    username = result.data.username,
                                                    email = result.data.email,
                                                    role = result.data.role,
                                                    subscriptionType = result.data.subscriptionType,
                                                    subscriptionExpiration = result.data.subscriptionExpiration
                                                )
                                            )
                                            authMessage = null
                                        }
                                        is UiState.Error -> authMessage = result.message
                                        UiState.Loading -> Unit
                                    }
                                }
                            }
                        } else {
                            scope.launch {
                                when (val result = repository.login(email.trim(), password)) {
                                    is UiState.Success -> {
                                        onAuthStateChange(
                                            AuthState(
                                                token = result.data.token,
                                                username = result.data.username,
                                                email = result.data.email,
                                                role = result.data.role,
                                                subscriptionType = result.data.subscriptionType,
                                                subscriptionExpiration = result.data.subscriptionExpiration
                                            )
                                        )
                                        authMessage = null
                                    }
                                    is UiState.Error -> authMessage = result.message
                                    UiState.Loading -> Unit
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isRegister) "Create Account" else "Sign In")
                }
                Button(
                    onClick = {
                        isRegister = !isRegister
                        authMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BazingaSurfaceAlt)
                ) {
                    Text(text = if (isRegister) "Have an account? Sign in" else "New here? Create an account")
                }
                if (authMessage != null) {
                    Text(text = authMessage!!, color = BazingaTextMuted)
                }
            } else {
                when (libraryState) {
                    UiState.Loading -> LoadingState(message = "Loading your library...")
                    is UiState.Error -> ErrorState(message = (libraryState as UiState.Error).message)
                    is UiState.Success -> {
                        val items = (libraryState as UiState.Success<List<LibraryItemDto>>).data
                        if (items.isEmpty()) {
                            Text(
                                text = "Your library is empty. Add a Digital Exclusive comic to start reading.",
                                color = BazingaTextMuted
                            )
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(items) { item ->
                                    LibraryItemCard(
                                        item = item,
                                        modifier = Modifier.fillMaxWidth(),
                                        onRead = { onReadComic(item.comic.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryItemCard(item: LibraryItemDto, modifier: Modifier = Modifier, onRead: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BazingaSurface)
            .padding(12.dp)
    ) {
    Box(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    ) {
            val image = resolveImageUrl(item.comic.image)
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = item.comic.title,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BazingaSurfaceAlt, BazingaSurface)
                            )
                        )
                )
            }
            if (item.comic.comicType == "ONLY_DIGITAL") {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BazingaRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Digital Exclusive",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Text(
            text = item.comic.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = item.comic.author ?: "Bazinga Studios",
            fontSize = 11.sp,
            color = BazingaTextMuted
        )
        Button(
            onClick = onRead,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Read now")
        }
    }
}
