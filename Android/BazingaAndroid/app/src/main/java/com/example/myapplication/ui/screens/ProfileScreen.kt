package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted

@Composable
fun ProfileScreen(
    authState: AuthState,
    onSignOut: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onProfileUpdate: (AuthState) -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var dateOfBirth by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState) {
        if (authState.token.isNotBlank()) {
            username = authState.username
            email = authState.email
            firstName = authState.firstName
            lastName = authState.lastName
            dateOfBirth = authState.dateOfBirth
            password = ""
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            if (authState.token.isBlank()) {
                Text(
                    text = "Sign in to view and manage your Bazinga profile.",
                    color = BazingaTextMuted
                )
                Button(onClick = onNavigateToLibrary, modifier = Modifier.fillMaxWidth()) {
                    Text("Go to Sign In")
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = authState.username, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = authState.email, color = BazingaTextMuted, fontSize = 12.sp)
                        Text(text = "Role: ${authState.role.ifBlank { "USER" }}", color = BazingaTextMuted, fontSize = 12.sp)
                        Text(
                            text = "Subscription: ${authState.subscriptionType ?: "Free"}",
                            color = BazingaTextMuted,
                            fontSize = 12.sp
                        )
                        authState.subscriptionExpiration?.let { expiration ->
                            Text(
                                text = "Renews on $expiration",
                                color = BazingaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Edit profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Keep your Bazinga profile up to date.", color = BazingaTextMuted, fontSize = 12.sp)
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = dateOfBirth,
                            onValueChange = { dateOfBirth = it },
                            label = { Text("Date of birth") },
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
                                onProfileUpdate(
                                    authState.copy(
                                        username = username.trim(),
                                        email = email.trim(),
                                        firstName = firstName.trim(),
                                        lastName = lastName.trim(),
                                        dateOfBirth = dateOfBirth.trim()
                                    )
                                )
                                password = ""
                                feedbackMessage = "Profile details saved."
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save changes")
                        }
                        Button(
                            onClick = {
                                username = authState.username
                                email = authState.email
                                firstName = authState.firstName
                                lastName = authState.lastName
                                dateOfBirth = authState.dateOfBirth
                                password = ""
                                feedbackMessage = "Changes reset."
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BazingaSurfaceAlt)
                        ) {
                            Text("Reset")
                        }
                        feedbackMessage?.let { message ->
                            Text(text = message, color = BazingaTextMuted, fontSize = 12.sp)
                        }
                    }
                }
                Button(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BazingaSurfaceAlt)
                ) {
                    Text("Sign out")
                }
            }
        }
    }
}
