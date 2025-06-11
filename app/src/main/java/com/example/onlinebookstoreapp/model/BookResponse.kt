package com.example.onlinebookstoreapp.model

import com.example.onlinebookstoreapp.Entities.BookEntity

// Main API response wrapper
data class BookResponse(
    val message: String,
    val data: BookData,
    val pagination: PaginationInfo
)

data class BookData(
    val books: List<Book>
)

data class PaginationInfo(
    val totalDocs: Int,
    val limit: Int,
    val page: Int,
    val total_pages: Int,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean
)

// Individual book model matching API structure
data class Book(
    val _id: String,
    val title: String,
    val author: String,
    val price: Double,
    val isbn: String?,
    val description: String?,
    val publicationDate: String?,
    val pageCount: Int?,
    val genres: List<String>?,
    val stock: Int,
    val coverImage: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toEntity(): BookEntity {
        return BookEntity(
            id = _id,
            title = title,
            author = author,
            price = price,
            description = description ?: "",
            imageUrl = coverImage ?: "",
            rating = 4.0f, // Default since API doesn't provide rating
            lastUpdated = System.currentTimeMillis()
        )
    }
}