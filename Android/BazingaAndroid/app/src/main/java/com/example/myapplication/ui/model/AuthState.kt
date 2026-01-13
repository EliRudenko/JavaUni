package com.example.myapplication.ui.model

data class AuthState(
    val token: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val subscriptionType: String? = null,
    val subscriptionExpiration: String? = null
)
