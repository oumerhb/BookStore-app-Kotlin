package com.example.onlinebookstoreapp.auth

import android.content.Context
import android.content.SharedPreferences

class AuthManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun saveUserInfo(name: String, email: String) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getUserEmail(): String? = prefs.getString("user_email", null)

    fun isLoggedIn(): Boolean {
        return getAuthToken() != null
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}