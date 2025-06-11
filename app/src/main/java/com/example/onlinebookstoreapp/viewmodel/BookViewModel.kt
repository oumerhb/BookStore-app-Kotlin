package com.example.onlinebookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.repository.BookstoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BookViewModel(
    private val repository: BookstoreRepository
) : ViewModel() {
    private val _books = MutableStateFlow<List<BookEntity>>(emptyList())
    val books: StateFlow<List<BookEntity>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.syncBooks()

            when (result) {
                is ApiResult.Success<List<BookEntity>> -> {
                    _books.value = result.data
                }
                is ApiResult.Failure -> {
                    _error.value = "Failed to load books: ${result.exception.message}"
                }
            }
            _isLoading.value = false
        }
    }
}
sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val exception: Throwable) : ApiResult<Nothing>()
}

inline fun <T> safeApiCall(block: () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: IOException) {
        ApiResult.Failure(e)
    } catch (e: HttpException) {
        ApiResult.Failure(e)
    } catch (e: Exception) {
        ApiResult.Failure(e)
    }
}