package com.example.onlinebookstoreapp.model

import com.example.onlinebookstoreapp.Entities.BookEntity

// Model/BookResponse.kt
data class BookResponse(
    val id: String,
    val title: String,
    val author: String,
    val price: Double,
    val rating: Float,
    val description: String,
    val imageUrl: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toEntity(): BookEntity {
        return BookEntity(
            id = id,
            title = title,
            author = author,
            price = price,
            description = description,
            imageUrl = imageUrl,
            rating =rating,
            lastUpdated = lastUpdated
        )
    }
}


