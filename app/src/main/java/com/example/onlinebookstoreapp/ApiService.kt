package com.example.onlinebookstoreapp

import com.example.onlinebookstoreapp.model.AddToCartRequest
import com.example.onlinebookstoreapp.model.BookResponse
import com.example.onlinebookstoreapp.model.CartItemResponse
import com.example.onlinebookstoreapp.model.CategoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BookstoreApiService {
    @GET("books")
    suspend fun getBooks(): List<BookResponse>

    @GET("books/{id}")
    suspend fun getBookDetails(@Path("id") id: String): BookResponse

    @GET("cart")
    suspend fun getCartItems(): List<CartItemResponse>

    @POST("cart")
    suspend fun addToCart(@Body request: AddToCartRequest): CartItemResponse
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>


    // Add other endpoints as needed
}