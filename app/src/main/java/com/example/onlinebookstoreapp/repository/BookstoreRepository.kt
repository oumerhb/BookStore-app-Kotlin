package com.example.onlinebookstoreapp.repository

import com.example.onlinebookstoreapp.AppDatabase
import com.example.onlinebookstoreapp.BookGenre
import com.example.onlinebookstoreapp.BookstoreApiService
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CartItemEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity
import com.example.onlinebookstoreapp.viewmodel.ApiResult
import com.example.onlinebookstoreapp.viewmodel.safeApiCall

// Repository/BookstoreRepository.kt
class BookstoreRepository(
    private val apiService: BookstoreApiService,
    private val db: AppDatabase
) {
    private val bookDao = db.bookDao()
    private val cartDao = db.cartDao()

    suspend fun getBooks(
        page: Int = 1,
        limit: Int = 10,
        sort: String? = null,
        search: String? = null,
        genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
            // Access the books through response.data.books, not directly from response
            val bookEntities = response.data.books.map { it.toEntity() }
            ApiResult.Success(bookEntities)
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }
    suspend fun syncCart(): ApiResult<List<CartItemEntity>> {
        return safeApiCall {
            val remoteCart = apiService.getCartItems()
            val cartEntities: List<CartItemEntity> = remoteCart.map { it.toEntity() }
            cartDao.insertCartItems(cartEntities)

            // Return fresh data from DB to ensure consistency
            cartDao.getCartItems().also {
                if (it.isEmpty()) throw IllegalStateException("Cart sync failed - no items found")
            }
        }
    }
    suspend fun getFeaturedBooks(page: Int = 1,
                                 limit: Int = 10,
                                 sort: String? = null,
                                 search: String? = null,
                                 genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
            // Access the books through response.data.books, not directly from response
            val bookEntities = response.data.books.map { it.toEntity() }
            ApiResult.Success(bookEntities)
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }

    suspend fun getHardcodedCategories(): ApiResult<List<CategoryEntity>> {
        return try {
            val categories = BookGenre.getAllGenres()
            ApiResult.Success(categories)
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }
    suspend fun getNewArrivals(page: Int = 1,
                               limit: Int = 10,
                               sort: String? = null,
                               search: String? = null,
                               genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
            // Access the books through response.data.books, not directly from response
            val bookEntities = response.data.books.map { it.toEntity() }
            ApiResult.Success(bookEntities)
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }
    // Add more repository methods as needed
}