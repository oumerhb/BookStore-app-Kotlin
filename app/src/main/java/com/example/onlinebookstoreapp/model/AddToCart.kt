package com.example.onlinebookstoreapp.model

data class CartItemRequest(
    val bookId: String,
    val quantity: Int
)
data class AddToCartRequest(
    val items: List<CartItemRequest> // The API expects an array of items
)