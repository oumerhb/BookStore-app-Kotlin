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
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BookstoreApiService {
    @GET("v1/books")
    suspend fun getBooks(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("search") search: String? = null,
        @Query("genres") genres: List<String>? = null,
        @Query("priceMin") priceMin: Double? = null,
        @Query("priceMax") priceMax: Double? = null
    ): BookResponse

    @GET("v1/books")
    suspend fun searchBooks(@QueryMap queryParams: Map<String, String>): BookResponse

    @GET("books/{id}")
    suspend fun getBookDetails(@Path("id") id: String): BookResponse

    @GET("cart")
    suspend fun getCartItems(): List<CartItemResponse>

    @POST("cart")
    suspend fun addToCart(@Body request: AddToCartRequest): CartItemResponse

    @GET("books")
    suspend fun getBooksByGenre(@Query("genres") genre: String): BookResponse

    // For multiple genres
    @GET("books")
    suspend fun getBooksByGenres(@Query("genres") genres: List<String>): BookResponse

    // Add other endpoints as needed
}