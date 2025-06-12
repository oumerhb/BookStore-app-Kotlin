package com.example.onlinebookstoreapp

import com.example.onlinebookstoreapp.model.AddToCartRequest
import com.example.onlinebookstoreapp.model.BookDetailResponse
import com.example.onlinebookstoreapp.model.BookResponse
import com.example.onlinebookstoreapp.model.CartItem
import com.example.onlinebookstoreapp.model.CartItemRequest
import com.example.onlinebookstoreapp.model.CartItemResponse
import com.example.onlinebookstoreapp.model.CategoryResponse
import com.example.onlinebookstoreapp.model.LoginResponse
import com.example.onlinebookstoreapp.model.LogoutResponse
import com.example.onlinebookstoreapp.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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
    @GET("v1/books/{id}")
    suspend fun getBookDetails(@Path("id") id: String): BookDetailResponse

    @POST("v1/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): CartItemResponse
    @GET("v1/cart")
    suspend fun getCartItems(): CartItemResponse
    @GET("books")
    suspend fun getBooksByGenre(@Query("genres") genre: String): BookResponse
    @PUT("v1/cart")
    suspend fun updateCartQuantities(@Body request: Map<String, Any>): CartItemResponse

    @DELETE("v1/cart/{bookId}")
    suspend fun removeBookFromCart(@Path("bookId") bookId: String): CartItemResponse
    @POST("v1/auth/login")
    suspend fun login(@Body request: Map<String, String>): LoginResponse
    @POST("v1/auth/register")
    suspend fun register(@Body request: Map<String, String>): RegisterResponse
    @POST("v1/auth/logout")
    suspend fun logout(): LogoutResponse
    @DELETE("v1/cart/clear")
    suspend fun clearCart(): CartItemResponse
    // For multiple genres
    @GET("books")
    suspend fun getBooksByGenres(@Query("genres") genres: List<String>): BookResponse

    // Add other endpoints as needed
}