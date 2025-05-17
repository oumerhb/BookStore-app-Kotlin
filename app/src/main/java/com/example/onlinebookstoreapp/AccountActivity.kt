package com.example.onlinebookstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Initialize views
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val ordersLayout = findViewById<LinearLayout>(R.id.ordersLayout)
        val inboxLayout = findViewById<LinearLayout>(R.id.inboxLayout)
        val savedItemsLayout = findViewById<LinearLayout>(R.id.savedItemsLayout)
        val recentlyViewedLayout = findViewById<LinearLayout>(R.id.recentlyViewedLayout)
        val recentlySearchedLayout = findViewById<LinearLayout>(R.id.recentlySearchedLayout)
        val addressBookLayout = findViewById<LinearLayout>(R.id.addressBookLayout)
        val accountManagementLayout = findViewById<LinearLayout>(R.id.accountManagementLayout)
        val closeAccountLayout = findViewById<LinearLayout>(R.id.closeAccountLayout)
        val logoutButton = findViewById<TextView>(R.id.logoutButton)

        // Set user info
        // TODO: Get user info from shared preferences or backend
        welcomeText.text = "Welcome Abdullah!"
        emailText.text = "Abdullah@gmail.com"

        // Set click listeners
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ordersLayout.setOnClickListener {
            // TODO: Navigate to orders screen
        }

        inboxLayout.setOnClickListener {
            // TODO: Navigate to inbox screen
        }

        savedItemsLayout.setOnClickListener {
            // TODO: Navigate to saved items screen
        }

        recentlyViewedLayout.setOnClickListener {
            // TODO: Navigate to recently viewed screen
        }

        recentlySearchedLayout.setOnClickListener {
            // TODO: Navigate to recently searched screen
        }

        addressBookLayout.setOnClickListener {
            // TODO: Navigate to address book screen
        }

        accountManagementLayout.setOnClickListener {
            // TODO: Navigate to account management screen
        }

        closeAccountLayout.setOnClickListener {
            // TODO: Show close account confirmation dialog
        }

        logoutButton.setOnClickListener {
            // TODO: Clear user session
            startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
    }
} 