package com.example.onlinebookstoreapp.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val title: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
