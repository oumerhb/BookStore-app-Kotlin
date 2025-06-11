package com.example.onlinebookstoreapp.model

data class AddToCartRequest(
    val bookId: String,
    val quantity: Int
)