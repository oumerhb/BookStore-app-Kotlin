package com.example.onlinebookstoreapp.repository

import com.example.onlinebookstoreapp.AppDatabase
import com.example.onlinebookstoreapp.BookGenre
import com.example.onlinebookstoreapp.BookstoreApiService
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CartItemEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity
import com.example.onlinebookstoreapp.viewmodel.ApiResult
import com.example.onlinebookstoreapp.viewmodel.safeApiCall

class BookstoreRepository private constructor(
    private val apiService: BookstoreApiService,
    private val db: AppDatabase
) {
    private val bookDao = db.bookDao()
    private val cartDao = db.cartDao()

    companion object {
        @Volatile
        private var INSTANCE: BookstoreRepository? = null

        fun getInstance(apiService: BookstoreApiService, db: AppDatabase): BookstoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BookstoreRepository(apiService, db).also { INSTANCE = it }
            }
        }

        fun getInstance(): BookstoreRepository {
            return INSTANCE ?: throw IllegalStateException("BookstoreRepository must be initialized first")
        }
    }

    suspend fun getBooks(
        page: Int = 1,
        limit: Int = 10,
        sort: String? = null,
        search: String? = null,
        genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
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

            cartDao.getCartItems().also {
                if (it.isEmpty()) throw IllegalStateException("Cart sync failed - no items found")
            }
        }
    }

    suspend fun getFeaturedBooks(
        page: Int = 1,
        limit: Int = 10,
        sort: String? = null,
        search: String? = null,
        genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
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

    suspend fun getNewArrivals(
        page: Int = 1,
        limit: Int = 10,
        sort: String? = null,
        search: String? = null,
        genres: List<String>? = null
    ): ApiResult<List<BookEntity>> {
        return try {
            val response = apiService.getBooks(page, limit, sort, search, genres)
            val bookEntities = response.data.books.map { it.toEntity() }
            ApiResult.Success(bookEntities)
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }
}