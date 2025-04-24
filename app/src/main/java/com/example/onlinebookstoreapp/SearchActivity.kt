package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var recentBooksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText)
        recentBooksRecyclerView = findViewById(R.id.recentBooksRecyclerView)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val voiceSearchButton = findViewById<ImageButton>(R.id.voiceSearchButton)

        // Set up RecyclerView
        recentBooksRecyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Set up adapter with recent books data

        // Set up search functionality
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        // Set click listeners
        backButton.setOnClickListener {
            onBackPressed()
        }

        voiceSearchButton.setOnClickListener {
            // TODO: Implement voice search
        }
    }

    private fun performSearch(query: String) {
        // TODO: Implement search functionality
    }

    // Data class for book items
    data class Book(
        val title: String,
        val coverImage: Int,
        val timestamp: Long
    )
} 