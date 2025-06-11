package com.example.onlinebookstoreapp.repository

import com.example.onlinebookstoreapp.AppDatabase
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

    suspend fun syncBooks(): ApiResult<List<BookEntity>> {
        return safeApiCall {
            val remoteBooks = apiService.getBooks()
            val bookEntities: List<BookEntity> = remoteBooks.map { it.toEntity() }
            bookDao.insertBooks(bookEntities)
            bookDao.getAllBooks() // Return the fresh list from DB
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
    suspend fun getFeaturedBooks(): ApiResult<List<BookEntity>> {
        return safeApiCall {
            val response = apiService.getBooks()
            val entities = response.map { it.toEntity() }
            db.bookDao().insertBooks(entities)
            entities
        }
    }

    suspend fun getCategories(): ApiResult<List<CategoryEntity>> {
        return safeApiCall {
            val response = apiService.getCategories()
            val entities = response.map { it.toEntity() }
            db.categoryDao().insertCategories(entities)
            entities
        }
    }

    suspend fun getNewArrivals(): ApiResult<List<BookEntity>> {
        return safeApiCall {
            val response = apiService.getBooks()
            val entities = response.map { it.toEntity() }
            db.bookDao().insertBooks(entities)
            entities
        }
    }
    // Add more repository methods as needed
}