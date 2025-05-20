package com.example.onlinebookstoreapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView



class BookDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
    }

    private var currentBook: Book? = null // To hold the loaded book details
    private var quantity = 1
    private var isWishlisted = false

    // Declare views
    private lateinit var ivBookCover: ImageView
    private lateinit var tvDiscountBanner: TextView
    private lateinit var tvBookTitle: TextView
    private lateinit var tvBookAuthor: TextView
    private lateinit var tvBookPrice: TextView
    private lateinit var ibWishlist: ImageButton
    private lateinit var tvQuantity: TextView
    private lateinit var btnQuantityMinus: ImageButton
    private lateinit var btnQuantityPlus: ImageButton
    private lateinit var btnAddToCart: Button
    private lateinit var btnBuyNow: Button

    private lateinit var llDescHeader: LinearLayout
    private lateinit var ivExpandDesc: ImageView
    private lateinit var tvDescContent: TextView
    private var isDescExpanded = false

    // For Book Details
    private lateinit var llDetailsHeader: LinearLayout
    private lateinit var ivExpandDetails: ImageView
    private lateinit var tvDetailsContent: TextView
    private var isDetailsExpanded = false

    // For Reviews
    private lateinit var llReviewsHeader: LinearLayout
    private lateinit var ivExpandReviews: ImageView
    private lateinit var tvReviewsContent: TextView
    private var isReviewsExpanded = false

    private lateinit var rvSimilarBooks: RecyclerView
    private lateinit var similarBooksAdapter: BookAdapter // Reuse your BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        val drawerLayout=findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            drawerLayout.close()
            true
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // No title, using icons

        // Initialize views
        ivBookCover = findViewById(R.id.iv_book_cover_detail)
        tvDiscountBanner = findViewById(R.id.tv_discount_banner)
        tvBookTitle = findViewById(R.id.tv_book_title_detail)
        tvBookAuthor = findViewById(R.id.tv_book_author_detail)
        tvBookPrice = findViewById(R.id.tv_book_price_detail)
        ibWishlist = findViewById(R.id.ib_wishlist)
        tvQuantity = findViewById(R.id.tv_quantity)
        btnQuantityMinus = findViewById(R.id.btn_quantity_minus)
        btnQuantityPlus = findViewById(R.id.btn_quantity_plus)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        btnBuyNow = findViewById(R.id.btn_buy_now)

        llDescHeader = findViewById(R.id.ll_book_description_header)
        ivExpandDesc = findViewById(R.id.iv_expand_description)
        tvDescContent = findViewById(R.id.tv_book_description_content)
        llDetailsHeader = findViewById(R.id.ll_book_details_header)
        ivExpandDetails = findViewById(R.id.iv_expand_details)
        tvDetailsContent = findViewById(R.id.tv_book_details_content)
        llReviewsHeader = findViewById(R.id.ll_reviews_header)
        ivExpandReviews = findViewById(R.id.iv_expand_reviews)
        tvReviewsContent = findViewById(R.id.tv_reviews_content)

        rvSimilarBooks = findViewById(R.id.rv_similar_books)

        val bookId = intent.getStringExtra(EXTRA_BOOK_ID)

        if (bookId != null) {
            currentBook = BookRepository.getBookById(bookId) // Use BookRepository
            if (currentBook == null) {
                Toast.makeText(this, "Book details not found.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            isWishlisted = UserDataRepository.isInWishlist(currentBook!!.id) // Check initial state
            populateBookDetails(currentBook!!)
            loadSimilarBooks(currentBook!!.id)
        } else {
            Toast.makeText(this, "Book ID missing.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupClickListeners()
        updateQuantityDisplay()
        updateWishlistIcon()
    }
    fun onDetailsToolbarCartIconClicked(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
            // These flags help bring MainActivity to the front if it exists,
            // or create a new one, and clear activities on top of it in this task.
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO_TAB", "cart") // Send a signal to MainActivity
        }
        startActivity(intent)
        finish() // Close BookDetailsActivity as we are navigating away
    }
    private fun populateBookDetails(book: Book) {
        tvBookTitle.text = book.title
        tvBookAuthor.text = book.author
        tvBookPrice.text = book.price ?: "N/A"
        ivBookCover.setImageResource(R.drawable.placeholder_book_cover_with_x)


        // Discount banner logic (example)
        if (book.discountPercent != null && book.discountPercent > 0) {
            tvDiscountBanner.text = "-${book.discountPercent}%"
            tvDiscountBanner.visibility = View.VISIBLE
        } else if (book.price?.contains("Sale", ignoreCase = true) == true) { // Fallback for string based sale
            tvDiscountBanner.text = "-SPECIAL-" // Or parse from price string
            tvDiscountBanner.visibility = View.VISIBLE
        }
        else {
            tvDiscountBanner.visibility = View.GONE
        }

        // Populate description, details (dummy data for now)
        tvDescContent.text = "This is a fascinating book about ${book.title.lowercase()} by ${book.author}. It explores many interesting concepts and is a must-read for enthusiasts. More details to come regarding its specific content and chapters."
        tvDetailsContent.text = "ISBN: ${book.id}-XYZ\nPages: (Pages)\nPublisher: (Publisher)\nLanguage: English"
        tvReviewsContent.text = "No reviews yet. Be the first to review!"

        // ... (other UI updates based on book data)
    }


    private fun setupClickListeners() {
        btnQuantityMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityDisplay()
            }
        }
        btnQuantityPlus.setOnClickListener {
            quantity++
            updateQuantityDisplay()
        }
        ibWishlist.setOnClickListener {
            currentBook?.let { book ->
                if (UserDataRepository.isInWishlist(book.id)) {
                    UserDataRepository.removeFromWishlist(book.id)
                    isWishlisted = false
                    Toast.makeText(this, "${book.title} removed from wishlist", Toast.LENGTH_SHORT).show()
                } else {
                    UserDataRepository.addToWishlist(book.id)
                    isWishlisted = true
                    Toast.makeText(this, "${book.title} added to wishlist", Toast.LENGTH_SHORT).show()
                }
                updateWishlistIcon()
            }
        }

        btnAddToCart.setOnClickListener {
            currentBook?.let { book ->
                UserDataRepository.addToCart(book.id, quantity)
                Toast.makeText(this, "${book.title} (x$quantity) added to cart", Toast.LENGTH_SHORT).show()
                updateToolbarCartBadge() // Update badge after adding to cart
            }
        }
        btnBuyNow.setOnClickListener {
            Toast.makeText(this, "Proceeding to checkout for ${currentBook?.title} x$quantity", Toast.LENGTH_SHORT).show()
            // Checkout logic here
        }

        // Expandable sections
        llDescHeader.setOnClickListener {
            isDescExpanded = !isDescExpanded
            tvDescContent.visibility = if (isDescExpanded) View.VISIBLE else View.GONE
            ivExpandDesc.setImageResource(if (isDescExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more)
        }

        llDetailsHeader.setOnClickListener {
            isDetailsExpanded = !isDetailsExpanded
            tvDetailsContent.visibility = if (isDetailsExpanded) View.VISIBLE else View.GONE
            ivExpandDetails.setImageResource(
                if (isDetailsExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            )
            // Optionally, load detailed content here if it's heavy and not needed initially
            if (isDetailsExpanded && tvDetailsContent.text.isBlank()) {
                // Example: currentBook?.let { tvDetailsContent.text = "ISBN: ${it.isbn}\nPages: ${it.pages}" }
                // For now, using the dummy text from XML if it's not already set
                if (tvDetailsContent.text.toString().contains("ISBN")) { // Check if placeholder is already there
                    // Do nothing if already populated (e.g. from loadBookDetails or XML tools:text)
                } else {
                    // Populate if empty
                    currentBook?.let { book ->
                        // You'd have these fields in your Book data class
                        // For now, let's use some placeholders
                        val detailsText = "ISBN: ${book.id}-XYZ\nPages: 250\nPublisher: Demo Publisher\nLanguage: English"
                        tvDetailsContent.text = detailsText
                    } ?: run {
                        tvDetailsContent.text = getString(R.string.details_not_available)
                    }
                }
            }
        }

        // Reviews
        llReviewsHeader.setOnClickListener {
            isReviewsExpanded = !isReviewsExpanded
            tvReviewsContent.visibility = if (isReviewsExpanded) View.VISIBLE else View.GONE
            ivExpandReviews.setImageResource(
                if (isReviewsExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            )
            // Optionally, load reviews here (e.g., from an API or a more complex local data source)
            if (isReviewsExpanded && tvReviewsContent.text.toString().contains("No reviews yet")) { // Or check if it's empty
                // tvReviewsContent.text = "Loading reviews..."
                // fetchReviewsForBook(currentBook?.id)
                // For now, we'll just show the placeholder or a dummy "loaded" state
                tvReviewsContent.text = getString(R.string.sample_reviews)
            }
            else if (!isReviewsExpanded && !tvReviewsContent.text.toString().contains("No reviews yet")) {
                // Optional: clear complex loaded content if you want to reload next time, or leave as is
                // For this example, we will let it persist once loaded.
                // If you want to revert to placeholder when collapsed:
              tvReviewsContent.text = "No reviews yet. Be the first to review!"
            }
        }

    }
    private fun updateToolbarCartBadge() {
        // Assuming tv_cart_badge_toolbar is the ID in activity_book_details.xml toolbar
        val cartBadge = findViewById<TextView>(R.id.tv_cart_badge_toolbar)
        val itemCount = UserDataRepository.getCartItemCount()
        if (itemCount > 0) {
            cartBadge.text = itemCount.toString()
            cartBadge.visibility = View.VISIBLE
        } else {
            cartBadge.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()
        updateToolbarCartBadge() // Ensure badge is up-to-date when returning
        currentBook?.let { // Re-check wishlist status
            isWishlisted = UserDataRepository.isInWishlist(it.id)
            updateWishlistIcon()
        }
    }

    private fun updateQuantityDisplay() {
        tvQuantity.text = quantity.toString()
    }

    private fun updateWishlistIcon() {
        if (isWishlisted) {
            ibWishlist.setImageResource(R.drawable.ic_wishlist_filled)
            ibWishlist.setColorFilter(ContextCompat.getColor(this, R.color.wishlist_red_color)) // Define this color
        } else {
            ibWishlist.setImageResource(R.drawable.ic_wishlist_outline)
            ibWishlist.clearColorFilter() // Or set to default tint
        }
    }


    private fun loadSimilarBooks(currentBookId: String) {
        // Get some books from the repository, excluding the current one
        val similar = BookRepository.allBooks
            .filter { it.id != currentBookId && it.category == currentBook?.category } // Same category, different book
            .shuffled()
            .take(5)

        if (::similarBooksAdapter.isInitialized) { // Check if adapter is initialized
            // If you want to update adapter data:
            // similarBooksAdapter.updateData(similar) // You'd need an updateData method in BookAdapter
        } else {
            similarBooksAdapter = BookAdapter(similar) { book ->
                val intent = Intent(this, BookDetailsActivity::class.java)
                intent.putExtra(EXTRA_BOOK_ID, book.id)
                startActivity(intent)
                finish() // Finish current details to avoid stacking same activity
            }
            rvSimilarBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvSimilarBooks.adapter = similarBooksAdapter
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed() // or finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}