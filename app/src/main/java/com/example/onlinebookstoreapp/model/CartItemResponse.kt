package com.example.onlinebookstoreapp.model

import com.example.onlinebookstoreapp.Entities.CartItemEntity


data class CartItemResponse(
    val id: String,
    val bookId: String,
    val title: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
) {
    fun toEntity(): CartItemEntity {
        return CartItemEntity(
            id = id,
            bookId = bookId,
            title = title,
            price = price,
            quantity = quantity,
            imageUrl = imageUrl
        )
    }
}
