package com.example.onlinebookstoreapp.model

import com.example.onlinebookstoreapp.Entities.CartItemEntity


data class CartItemResponse(
    val message: String,
    val data: CartData
)

data class CartData(
    val cart: Cart
)

data class Cart(
    val id: String,
    val user: String,
    val items: List<CartItem>,
    val createdAt: String,
    val updatedAt: String
)

data class CartItem(
    val book: BookInCart, // Book ID
    val quantity: Int
)

data class BookInCart(
    val id: String,
    val title: String,
    val author: String,
    val price: Double,
    val coverImage: String?
)