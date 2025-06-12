package com.example.onlinebookstoreapp.model

data class BookDetailResponse(
    val message: String,
    val data: BookDetailData
)

data class BookDetailData(
    val book: Book
)
