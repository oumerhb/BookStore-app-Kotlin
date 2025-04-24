package com.example.onlinebookstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val usernameInput = findViewById<TextInputEditText>(R.id.usernameInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPasswordText)
        val facebookButton = findViewById<ImageButton>(R.id.facebookButton)
        val googleButton = findViewById<ImageButton>(R.id.googleButton)
        val signUpText = findViewById<TextView>(R.id.signUpText)

        // Set click listeners
        backButton.setOnClickListener {
            onBackPressed()
        }

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            
            // TODO: Implement login logic
            if (username.isNotEmpty() && password.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        forgotPasswordText.setOnClickListener {
            // TODO: Implement forgot password functionality
        }

        facebookButton.setOnClickListener {
            // TODO: Implement Facebook login
        }

        googleButton.setOnClickListener {
            // TODO: Implement Google login
        }

        signUpText.setOnClickListener {
            // TODO: Implement sign up navigation
        }
    }
} 