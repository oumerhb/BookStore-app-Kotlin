package com.example.onlinebookstoreapp
// BookDetailsActivity.kt
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
// import com.bumptech.glide.Glide // If using Glide

class BookDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
        // const val EXTRA_BOOK_OBJECT = "extra_book_object" // If passing Parcelable
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

        val toolbar: Toolbar = findViewById(R.id.toolbar_book_details)
        setSupportActionBar(toolbar)
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
            loadBookDetails(bookId)
            loadSimilarBooks(bookId) // You'd likely get similar books based on the current book
        } else {
            Toast.makeText(this, "Book not found", Toast.LENGTH_LONG).show()
            finish() // Close activity if no book ID
            return
        }

        setupClickListeners()
        updateQuantityDisplay()
        updateWishlistIcon()
    }

    private fun loadBookDetails(bookId: String) {
        // --- In a real app, fetch this from a database or API ---
        // For now, let's simulate finding it from a predefined list or creating a dummy one
        currentBook = getDummyBookById(bookId) // Implement this function

        currentBook?.let { book ->
            tvBookTitle.text = book.title
            tvBookAuthor.text = book.author
            tvBookPrice.text = book.price ?: "N/A" // Handle null price

            // Simulate discount
            if (bookId == "1" || bookId == "4") { // Example: book "1" or "4" has discount
                tvDiscountBanner.text = "-30%"
                tvDiscountBanner.visibility = View.VISIBLE
            } else {
                tvDiscountBanner.visibility = View.GONE
            }

            // Load image using Glide or Picasso, or set a placeholder
            // Glide.with(this).load(book.imageUrl).placeholder(R.drawable.placeholder_book_cover_with_x).into(ivBookCover)
            ivBookCover.setImageResource(R.drawable.placeholder_book_cover_with_x) // Placeholder
            if (book.title == "Don't Make Me Think") { // Specific image for demo
                ivBookCover.setImageResource(R.drawable.placeholder_book_cover_with_x) // You'll need to add this image
            }


            // Populate description, details, reviews (dummy data for now)
            tvDescContent.text = "This is a fascinating book about ${book.title.lowercase()} by ${book.author}. It explores many interesting concepts and is a must-read for enthusiasts."
            // ... and so on for other sections
        }
    }

    private fun getDummyBookById(bookId: String): Book? {
        // This is just a placeholder. In a real app, this would query your data source.
        val allBooks = listOf(
            Book("1", "The Midnight Library", "Matt Haig", price = "EGP 250.00", imageUrl = "url1"),
            Book("2", "Klara and the Sun", "Kazuo Ishiguro", price = "EGP 300.00", imageUrl = "url2"),
            Book("3", "Project Hail Mary", "Andy Weir", price = "EGP 280.00", imageUrl = "url3"),
            Book("4", "Atomic Habits", "James Clear", price = "EGP 220.00", imageUrl = "url4"),
            Book("dm", "Don't Make Me Think", "Steve Krug", price = "EGP 35.46", imageUrl = "url_dmmt") // Special ID for the sample image
        )
        return allBooks.find { it.id == bookId } ?: if (bookId == "dm") Book("dm", "Don't Make Me Think", "Steve Krug", price = "EGP 35.46", imageUrl = "url_dmmt") else null
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
            isWishlisted = !isWishlisted
            updateWishlistIcon()
            val message = if (isWishlisted) "Added to wishlist" else "Removed from wishlist"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        btnAddToCart.setOnClickListener {
            Toast.makeText(this, "${currentBook?.title} x$quantity added to cart", Toast.LENGTH_SHORT).show()
            // Add to cart logic here
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
                        tvDetailsContent.text = "Details not available."
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
                tvReviewsContent.text = "Review 1: Great book!\nReview 2: Very insightful."
            }
            else if (!isReviewsExpanded && !tvReviewsContent.text.toString().contains("No reviews yet")) {
                // Optional: clear complex loaded content if you want to reload next time, or leave as is
                // For this example, we will let it persist once loaded.
                // If you want to revert to placeholder when collapsed:
              tvReviewsContent.text = "No reviews yet. Be the first to review!"
            }
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
    // Add to colors.xml: <color name="wishlist_red_color">#E53935</color>


    private fun loadSimilarBooks(currentBookId: String) {
        // Dummy similar books - in real app, get this from API/DB
        val similar = listOf(
            Book("s1", "JavaScript and JQuery", "Jon Duckett", price = "EGP 180", imageUrl="js_jquery_cover"),
            Book("s2", "Responsive Web Design", "Ethan Marcotte", price = "EGP 150", imageUrl="responsive_web_cover"),
            Book("s3", "Neuro Web Design", "Susan Weinschenk", price = "EGP 170", imageUrl="neuro_web_cover")
        )
        // You'd filter out the currentBookId if it could appear here

        similarBooksAdapter = BookAdapter(similar) { book ->
            // When a similar book is clicked, you might open another BookDetailsActivity
            val intent = Intent(this, BookDetailsActivity::class.java)
            intent.putExtra(EXTRA_BOOK_ID, book.id)
            startActivity(intent)
            // Potentially finish this one or let the back stack handle it
        }
        rvSimilarBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSimilarBooks.adapter = similarBooksAdapter
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