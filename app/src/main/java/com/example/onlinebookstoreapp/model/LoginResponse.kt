package com.example.onlinebookstoreapp.model

data class LoginResponse(
    val message: String,
    val data: LoginData
)

data class LoginData(
    val user: User,
    val accessToken: AccessToken
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)

data class AccessToken(
    val token: String,
    val expires: String
)

data class LogoutResponse(
    val message: String
)