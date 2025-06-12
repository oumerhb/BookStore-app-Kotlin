package com.example.onlinebookstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.databinding.ActivityBookDetailBinding
import com.example.onlinebookstoreapp.model.CartItemRequest
import com.example.onlinebookstoreapp.model.AddToCartRequest
import kotlinx.coroutines.launch

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private val apiService: BookstoreApiService by lazy {
        RetrofitClient.apiService
    }

    private lateinit var authManager: AuthManager
    private var currentBookId: String? = null

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize AuthManager
        authManager = AuthManager.getInstance(this)

        val bookId = intent.getStringExtra(EXTRA_BOOK_ID)
        if (bookId != null) {
            currentBookId = bookId
            loadBookDetails(bookId)
            setupClickListeners()
        } else {
            finish()
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.btnAddToCart.setOnClickListener {
            currentBookId?.let { bookId ->
                if (authManager.isLoggedIn()) {
                    addToCart(bookId)
                } else {
                    navigateToLogin()
                }
            }
        }

        binding.btnBuyNow.setOnClickListener {
            currentBookId?.let { bookId ->
                if (authManager.isLoggedIn()) {
                    // For Buy Now, add to cart first then navigate to checkout
                    addToCart(bookId, navigateToCheckout = true)
                } else {
                    navigateToLogin()
                }
            }
        }
    }

    private fun navigateToLogin() {
        Toast.makeText(this, "Please log in to add items to cart", Toast.LENGTH_SHORT).show()

        // Navigate to MainActivity and show login fragment
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("show_login", true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

    private fun loadBookDetails(bookId: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = apiService.getBookDetails(bookId)
                val book = response.data.book.toEntity()

                displayBookDetails(book)
            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@BookDetailActivity, "Error loading book details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayBookDetails(book: com.example.onlinebookstoreapp.Entities.BookEntity) {
        binding.progressBar.visibility = android.view.View.GONE

        binding.apply {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvPrice.text = "$${String.format("%.2f", book.price)}"
            tvDescription.text = book.description
            ratingBar.rating = book.rating

            Glide.with(this@BookDetailActivity)
                .load(book.imageUrl)
                .placeholder(R.drawable.book)
                .error(R.drawable.book)
                .into(ivBookCover)
        }
    }

    private fun addToCart(bookId: String, quantity: Int = 1, navigateToCheckout: Boolean = false) {
        // Disable buttons to prevent multiple clicks
        binding.btnAddToCart.isEnabled = false
        binding.btnBuyNow.isEnabled = false

        lifecycleScope.launch {
            try {
                val addToCartRequest = AddToCartRequest( // Wrap items in a list
                    items = listOf(CartItemRequest(bookId, quantity))
                )

                val response = apiService.addToCart(addToCartRequest)

                // Show success message
                val message = if (navigateToCheckout) {
                    "Added to cart! Proceeding to checkout..."
                } else {
                    "Book added to cart successfully!"
                }
                Toast.makeText(this@BookDetailActivity, message, Toast.LENGTH_SHORT).show()

                if (navigateToCheckout) {
                    // Navigate to cart/checkout screen
                    val intent = Intent(this@BookDetailActivity, MainActivity::class.java).apply {
                        putExtra("show_cart", true)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@BookDetailActivity,
                    "Failed to add to cart: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                // Re-enable buttons
                binding.btnAddToCart.isEnabled = true
                binding.btnBuyNow.isEnabled = true
            }
        }
    }
}