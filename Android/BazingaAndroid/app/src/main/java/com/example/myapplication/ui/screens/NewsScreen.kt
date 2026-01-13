package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.NewsPostDto
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.components.ErrorState
import com.example.myapplication.ui.components.LoadingState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaTextMuted
import com.example.myapplication.ui.util.formatNewsDate
import kotlinx.coroutines.launch

@Composable
fun NewsScreen(
    repository: BazingaRepository,
    authState: AuthState
) {
    var newsState by remember { mutableStateOf<UiState<List<NewsPostDto>>>(UiState.Loading) }
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var isSubmitting by rememberSaveable { mutableStateOf(false) }
    val canPost = authState.role == "ADMIN" || authState.role == "EDITOR"
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        newsState = repository.fetchNews()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Bazinga Newsroom", fontSize = 12.sp, color = BazingaRed, fontWeight = FontWeight.Bold)
                    Text(text = "Latest community news", fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = "Updates from Bazinga editors and administrators. Posts stay live for seven days.",
                        color = BazingaTextMuted
                    )
                }
            }
            if (canPost) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = BazingaSurface
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "Create a news post", fontWeight = FontWeight.Bold)
                            TextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            TextField(
                                value = content,
                                onValueChange = { content = it },
                                label = { Text("Details") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 4
                            )
                            Button(
                                onClick = {
                                    if (authState.token.isBlank()) return@Button
                                    isSubmitting = true
                                    scope.launch {
                                        when (val result = repository.postNews(authState.token, title, content)) {
                                            is UiState.Success -> {
                                                title = ""
                                                content = ""
                                                newsState = repository.fetchNews()
                                            }
                                            is UiState.Error -> Unit
                                            UiState.Loading -> Unit
                                        }
                                        isSubmitting = false
                                    }
                                },
                                enabled = !isSubmitting
                            ) {
                                Text(text = if (isSubmitting) "Posting..." else "Post news")
                            }
                        }
                    }
                }
            }
            item {
                when (newsState) {
                    UiState.Loading -> LoadingState(message = "Loading news...")
                    is UiState.Error -> ErrorState(message = (newsState as UiState.Error).message)
                    is UiState.Success -> Unit
                }
            }
            if (newsState is UiState.Success) {
                val posts = (newsState as UiState.Success<List<NewsPostDto>>).data
                if (posts.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = BazingaSurface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "No news yet", fontWeight = FontWeight.Bold)
                                Text(
                                    text = "Check back later for updates from the team.",
                                    color = BazingaTextMuted
                                )
                            }
                        }
                    }
                } else {
                    items(posts) { post ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = BazingaSurface
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = post.title, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "Posted by ${post.authorUsername} (${post.authorRole}) · ${formatNewsDate(post.createdAt)} · Expires ${formatNewsDate(post.expiresAt)}",
                                    fontSize = 11.sp,
                                    color = BazingaTextMuted
                                )
                                Text(text = post.content, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
