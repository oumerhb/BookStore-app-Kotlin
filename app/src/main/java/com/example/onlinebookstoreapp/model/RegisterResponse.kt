package com.example.onlinebookstoreapp.model

data class RegisterResponse(
    val message: String,
    val data: RegisterData
)

data class RegisterData(
    val user: User // Re-use the User data class from LoginResponse
)
