package com.example.onlinebookstoreapp

data class FilterOption(
    val id: String,
    val text: String,
    var isSelected: Boolean = false
)