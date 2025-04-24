package com.example.onlinebookstoreapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var usernameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var requirement1: TextView
    private lateinit var requirement2: TextView
    private lateinit var requirement3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        requirement1 = findViewById(R.id.requirement1)
        requirement2 = findViewById(R.id.requirement2)
        requirement3 = findViewById(R.id.requirement3)
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val signInText = findViewById<TextView>(R.id.signInText)

        // Set up password validation
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }
        })

        // Set click listeners
        nextButton.setOnClickListener {
            if (validateForm()) {
                // TODO: Implement account creation
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateForm(): Boolean {
        val username = usernameInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        var isValid = true

        if (username.isEmpty()) {
            (usernameInput.parent.parent as TextInputLayout).error = "Username is required"
            isValid = false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            (emailInput.parent.parent as TextInputLayout).error = "Valid email is required"
            isValid = false
        }

        if (password.isEmpty() || password.length < 8 || !password.any { it.isDigit() || it.isLetterOrDigit().not() }) {
            (passwordInput.parent.parent as TextInputLayout).error = "Password does not meet requirements"
            isValid = false
        }

        return isValid
    }

    private fun validatePassword(password: String) {
        // Check if password contains username or email
        val containsPersonalInfo = password.contains(usernameInput.text.toString(), true) ||
                password.contains(emailInput.text.toString(), true)
        requirement1.setTextColor(if (!containsPersonalInfo) getColor(R.color.green) else getColor(R.color.gray))

        // Check password length
        requirement2.setTextColor(if (password.length >= 8) getColor(R.color.green) else getColor(R.color.gray))

        // Check if password contains symbol or number
        requirement3.setTextColor(if (password.any { it.isDigit() || it.isLetterOrDigit().not() })
            getColor(R.color.green) else getColor(R.color.gray))
    }
} 