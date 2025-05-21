package com.example.onlinebookstoreapp

sealed class HomeScreenItem {
    data class CategoryRow(val category: Category) : HomeScreenItem()
    data class FilterRow(val filters: List<FilterOption>, val title: String = "Filters") : HomeScreenItem()
    data class CategoryGridRow(val category: Category) : HomeScreenItem()
    data class CartItemEntry(val book: Book, var quantity: Int) : HomeScreenItem()
    data class CartSummary(val totalItems: Int, val totalPrice: Double) : HomeScreenItem()
    data class EmptyStateItem(val message: String, val iconResId: Int? = null): HomeScreenItem()
}