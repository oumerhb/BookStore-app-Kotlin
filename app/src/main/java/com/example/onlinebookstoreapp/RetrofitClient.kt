package com.example.onlinebookstoreapp

import android.content.Context
import androidx.room.Room
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.repository.BookstoreRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val gson = GsonBuilder().create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val token = getStoredAccessToken()

            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        }
        .build()

    private fun getStoredAccessToken(): String? {
        return authManager?.getAuthToken()
    }
    val apiService: BookstoreApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BookstoreApiService::class.java)
    }

    private var database: AppDatabase? = null
    private var authManager: AuthManager? = null
    fun initialize(context: Context) {
        authManager = AuthManager.getInstance(context)
        // Initialize database if not already initialized
        if (database == null) {
            database = AppDatabase.getDatabase(context)
        }

        // Initialize the BookstoreRepository singleton
        BookstoreRepository.getInstance(apiService, database!!)
    }

    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("RetrofitClient must be initialized first")
    }
}