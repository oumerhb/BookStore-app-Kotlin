package com.example.onlinebookstoreapp

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String? = null, // For network images, or use Int for drawable resource
    val price: String? = null, // Or a more specific Price object
    val discountPercent: Int? = null
)
