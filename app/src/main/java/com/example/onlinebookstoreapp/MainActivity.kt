package com.example.onlinebookstoreapp

// MainActivity.kt (or your Fragment)
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var homeScreenAdapter: HomeScreenAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout =
            findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayoutMain)
        val toolbar: Toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_main)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            drawerLayout.close()
            true
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)


        mainRecyclerView = findViewById(R.id.rv_main_content)
        setupRecyclerView()


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadHomeData()
                    true
                }

                R.id.nav_library -> {
                    loadLibraryData()
                    true
                }
                R.id.nav_wishlist -> { // Ensure this ID is in bottom_nav_menu.xml
                    loadWishlistData()
                    true
                }
                R.id.nav_cart -> { // Ensure this ID is in bottom_nav_menu.xml
                    loadCartData()
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home // Select home by default
        }
        updateToolbarCartBadge()
    }

    override fun onResume() {
        super.onResume()
        updateToolbarCartBadge()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when (bottomNav.selectedItemId) {
            R.id.nav_wishlist -> loadWishlistData()
            R.id.nav_cart -> loadCartData()
            // Home and Library might also need refresh if their underlying data can change
            // while the app is in background, but typically less critical than wishlist/cart.
        }
    }
    fun onMainCartIconClicked(view: View) {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_cart
    }


    private fun updateToolbarCartBadge() {
        val cartBadgeTextView = findViewById<TextView>(R.id.tv_cart_badge_toolbar_main) // Ensure this ID is correct
        val itemCount = UserDataRepository.getCartItemCount()
        if (itemCount > 0) {
            cartBadgeTextView.text = itemCount.toString()
            cartBadgeTextView.visibility = View.VISIBLE
        } else {
            cartBadgeTextView.visibility = View.GONE
        }
    }
    private fun setupRecyclerView() {
        homeScreenAdapter = HomeScreenAdapter(
            emptyList(),
            onFilterClicked = { filterOption ->
                Toast.makeText(this, "Filter: ${filterOption.text}", Toast.LENGTH_SHORT).show()
                // Implement filter logic if needed
            },
            onSeeAllClicked = { categoryTitle ->
                Toast.makeText(this, "See all: $categoryTitle", Toast.LENGTH_SHORT).show()
                // Navigate to a screen showing all books in that category
            },
            onBookClicked = { book ->
                val intent = Intent(this, BookDetailsActivity::class.java)
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.id)
                startActivity(intent)
            }
        )
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeScreenAdapter
        }
        // loadHomeData() // Load initial data after adapter is set
    }

    private fun loadHomeData(filterCategory: String = "All") {
        supportActionBar?.title = if (filterCategory == "All") "Bookstore" else filterCategory
        val allBooksForHome = BookRepository.allBooks // Use BookRepository

        val homeScreenItems = mutableListOf<HomeScreenItem>()
        if (filterCategory.equals("All", ignoreCase = true)) {
            // Show multiple sections for "All"
            homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Recently read", allBooksForHome.shuffled().take(4))))
            homeScreenItems.add(HomeScreenItem.FilterRow( // Example filters
                listOf(FilterOption("f1", "Fiction"), FilterOption("f2", "Science"), FilterOption("f3", "Design"))
            ))
            homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Best seller", allBooksForHome.shuffled().take(5))))
            homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Books on sale", allBooksForHome.filter { it.price?.contains("Sale", true) == true || (it.discountPercent ?: 0) > 0 }.take(4))))
        } else {
            // Show a single list for the selected category
            val filteredBooks = allBooksForHome.filter { it.category.equals(filterCategory, ignoreCase = true) }
            if (filteredBooks.isNotEmpty()) {
                homeScreenItems.add(HomeScreenItem.CategoryRow(Category(filterCategory, filteredBooks)))
            } else {
                homeScreenItems.add(HomeScreenItem.CategoryRow(Category("No books in $filterCategory", emptyList())))
            }
            // Optionally add filters row here too if desired for filtered views
        }
        homeScreenAdapter.updateData(homeScreenItems)
    }

    private fun loadLibraryData() {
        supportActionBar?.title = "My Library"
        // For demo, show a sample of books. In a real app, this would be user's "owned" books.
        val libraryBooksSample = BookRepository.allBooks.shuffled().take(10)
        val booksByCategory = libraryBooksSample.groupBy { it.category }
        val libraryScreenItems = mutableListOf<HomeScreenItem>()

        booksByCategory.forEach { (categoryName, booksInCategory) ->
            libraryScreenItems.add(HomeScreenItem.CategoryGridRow(Category(categoryName, booksInCategory)))
        }
        if (libraryScreenItems.isEmpty()) {
            libraryScreenItems.add(HomeScreenItem.CategoryGridRow(Category("Your Library is Empty", emptyList())))
        }
        homeScreenAdapter.updateData(libraryScreenItems)
    }

    private fun loadWishlistData() {
        supportActionBar?.title = "My Wishlist"
        val wishlistIds = UserDataRepository.getWishlistBookIds()
        val wishlistBooks = BookRepository.getBooksByIds(wishlistIds)
        val wishlistScreenItems = mutableListOf<HomeScreenItem>()

        if (wishlistBooks.isNotEmpty()) {
            // Display as a single grid. You can choose CategoryRow for a list.
            wishlistScreenItems.add(HomeScreenItem.CategoryGridRow(Category("Wishlisted Books (${wishlistBooks.size})", wishlistBooks)))
        } else {
            wishlistScreenItems.add(HomeScreenItem.CategoryGridRow(Category("Your Wishlist is Empty", emptyList())))
        }
        homeScreenAdapter.updateData(wishlistScreenItems)
    }

    private fun loadCartData() {
        supportActionBar?.title = "Shopping Cart"
        val cartDataMap = UserDataRepository.getCartItems() // Map<BookID, Quantity>
        val cartBookIds = cartDataMap.keys
        val booksInCartDetails = BookRepository.getBooksByIds(cartBookIds)
        val cartScreenItems = mutableListOf<HomeScreenItem>()

        if (booksInCartDetails.isNotEmpty()) {
            // For now, displaying as a simple list. A real cart needs a custom item layout.
            // To show quantity, you'd need to modify BookAdapter or create a new CartAdapter.
            // For simplicity, we'll just list the books.
            val cartBooksForDisplay = booksInCartDetails.map { book ->
                val quantity = cartDataMap[book.id] ?: 0
                // You could augment the book title or use a custom data class for display
                // Book(id=book.id, title="${book.title} (x$quantity)", ...rest of book properties)
                book // Returning original book for now
            }
            cartScreenItems.add(HomeScreenItem.CategoryRow(Category("Items in Cart: ${UserDataRepository.getCartItemCount()}", cartBooksForDisplay)))
            // TODO: Add a section for Total Price and Checkout Button. This would be a new HomeScreenItem type.
        } else {
            cartScreenItems.add(HomeScreenItem.CategoryRow(Category("Your Cart is Empty", emptyList())))
        }
        homeScreenAdapter.updateData(cartScreenItems)
    }
}
