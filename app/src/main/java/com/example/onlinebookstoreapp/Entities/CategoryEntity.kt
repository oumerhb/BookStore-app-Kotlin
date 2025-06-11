package com.example.onlinebookstoreapp.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlinebookstoreapp.model.CategoryResponse

// entities/CategoryEntity.kt
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "book_count")
    val bookCount: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromResponse(response: CategoryResponse): CategoryEntity {
            return CategoryEntity(
                id = response.id,
                name = response.name,
                bookCount = response.bookCount,
                imageUrl = response.imageUrl
            )
        }
    }
}
