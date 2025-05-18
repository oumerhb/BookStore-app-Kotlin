package com.example.onlinebookstoreapp

sealed class HomeScreenItem {
    data class CategoryRow(val category: Category) : HomeScreenItem()
    data class FilterRow(val filters: List<FilterOption>, val title: String = "Filters") : HomeScreenItem()
    data class CategoryGridRow(val category: Category) : HomeScreenItem()
// Add other types like Banners, etc. if needed
}