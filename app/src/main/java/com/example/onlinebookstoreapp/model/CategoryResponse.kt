package com.example.onlinebookstoreapp.model

import com.example.onlinebookstoreapp.Entities.CategoryEntity
import com.google.gson.annotations.SerializedName

// api/models/CategoryResponse.kt
data class CategoryResponse(
    val id: String,
    val name: String,
    @SerializedName("book_count")
    val bookCount: Int,
    @SerializedName("image_url")
    val imageUrl: String
) {
    fun toEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            name = name,
            bookCount = bookCount,
            imageUrl = imageUrl
        )
    }
}
