package com.example.onlinebookstoreapp.viewmodel

import retrofit2.HttpException
import java.io.IOException

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