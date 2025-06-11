package com.example.onlinebookstoreapp

import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity

// Mappers.kt
fun BookEntity.toUiModel(): BookUiModel {
    return BookUiModel(
        id = id,
        title = title,
        author = author,
        rating = rating,
        price = price,
        imageUrl = imageUrl
    )
}

fun CategoryEntity.toUiModel(): CategoryUiModel {
    return CategoryUiModel(
        id = id,
        name = name,
        bookCount = bookCount,
        imageUrl = imageUrl
    )
}

// UI Models
data class BookUiModel(
    val id: String,
    val title: String,
    val author: String,
    val rating: Float,
    val price: Double,
    val imageUrl: String
)

data class CategoryUiModel(
    val id: String,
    val name: String,
    val bookCount: Int,
    val imageUrl: String
)