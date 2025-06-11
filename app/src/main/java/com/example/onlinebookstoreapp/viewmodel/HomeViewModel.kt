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

class HomeViewModel : ViewModel() {
    private val repository = BookstoreRepository.getInstance()

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
                val featuredDeferred = async {
                    repository.getBooks(
                        page = 1,
                        limit = 10,
                        sort = "price:desc"
                    )
                }

                val categoriesDeferred = async {
                    repository.getHardcodedCategories()
                }

                val arrivalsDeferred = async {
                    repository.getBooks(
                        page = 1,
                        limit = 10,
                        sort = "createdAt:desc"
                    )
                }

                val featuredResult = featuredDeferred.await()
                val categoriesResult = categoriesDeferred.await()
                val arrivalsResult = arrivalsDeferred.await()

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