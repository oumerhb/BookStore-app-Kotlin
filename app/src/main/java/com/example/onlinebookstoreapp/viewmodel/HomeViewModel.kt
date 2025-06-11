package com.example.onlinebookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity
import com.example.onlinebookstoreapp.repository.BookstoreRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BookstoreRepository
) : ViewModel() {
    private val _featuredBooks = MutableStateFlow<List<BookEntity>>(emptyList())
    val featuredBooks: StateFlow<List<BookEntity>> = _featuredBooks

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _newArrivals = MutableStateFlow<List<BookEntity>>(emptyList())
    val newArrivals: StateFlow<List<BookEntity>> = _newArrivals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Load all data in parallel using the actual API endpoints
                val featuredDeferred = async {
                    // Featured books: sort by price descending (assuming higher price = featured)
                    repository.getBooks(
                        page = 1,
                        limit = 10,
                        sort = "price:desc"
                    )
                }

                val categoriesDeferred = async {
                    // Categories: use hardcoded genres since API doesn't have categories endpoint
                    repository.getHardcodedCategories()
                }

                val arrivalsDeferred = async {
                    // New arrivals: sort by creation date descending
                    repository.getBooks(
                        page = 1,
                        limit = 10,
                        sort = "createdAt:desc"
                    )
                }

                // Collect results
                val featuredResult = featuredDeferred.await()
                val categoriesResult = categoriesDeferred.await()
                val arrivalsResult = arrivalsDeferred.await()

                // Handle results
                featuredResult.collectResult(_featuredBooks)
                categoriesResult.collectResult(_categories)
                arrivalsResult.collectResult(_newArrivals)

            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun <T> ApiResult<T>.collectResult(stateFlow: MutableStateFlow<T>) {
        when (this) {
            is ApiResult.Success -> stateFlow.value = data
            is ApiResult.Failure -> _error.value = exception.message
        }
    }
}