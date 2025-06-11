package com.example.onlinebookstoreapp.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val price: Double,
    val rating: Float,
    val description: String,
    val imageUrl: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
