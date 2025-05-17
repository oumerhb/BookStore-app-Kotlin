package com.example.onlinebookstoreapp

// MainActivity.kt (or your Fragment)
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var homeScreenAdapter: HomeScreenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: com.google.android.material.appbar.MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // For hamburger menu action if you have a NavDrawer
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)


        mainRecyclerView = findViewById(R.id.rv_main_content)
        setupRecyclerView()
        loadData()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                // Handle other items
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        homeScreenAdapter = HomeScreenAdapter(
            emptyList(),
            onFilterClicked = { filterOption ->
                Toast.makeText(this, "Filter: ${filterOption.text}", Toast.LENGTH_SHORT).show()
            },
            onSeeAllClicked = { categoryTitle ->
                Toast.makeText(this, "See all: $categoryTitle", Toast.LENGTH_SHORT).show()
            },
            onBookClicked = { book -> // This is the callback from HomeScreenAdapter
                val intent = Intent(this, BookDetailsActivity::class.java)
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.id)
                // If your Book class is Parcelable, you can pass the whole object:
                // intent.putExtra(BookDetailsActivity.EXTRA_BOOK_OBJECT, book)
                startActivity(intent)
            }
        )
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeScreenAdapter
        }
        loadData()
    }

    private fun loadData() {
        // Sample Data
        val recentlyReadBooks = listOf(
            Book("1", "The Midnight Library", "Matt Haig", price = "$10.99"),
            Book("2", "Klara and the Sun", "Kazuo Roguish", price = "$12.50"),
            Book("3", "Project Hail Mary", "Andy Weir", price = "$11.00")
        )
        val bestSellerBooks = listOf(
            Book("4", "Atomic Habits", "James Clear", price = "$9.99"),
            Book("5", "The Vanishing Half", "Brit Bennett", price = "$14.00"),
            Book("6", "Where the Crawdads Sing", "Delia Owens", price = "$8.50"),
            Book("7", "Another Great Book", "Some Author", price = "$13.20")
        )
        val saleBooks = listOf(
            Book("8", "Old Classic", "Famous Writer", price = "$5.00 (Sale)"),
            Book("9", "Hidden Gem", "New Talent", price = "$4.50 (Sale)")
        )

        val filters = listOf(
            FilterOption("f1", "Fiction"),
            FilterOption("f2", "Non-Fiction"),
            FilterOption("f3", "Sci-Fi"),
            FilterOption("f4", "Mystery"),
            FilterOption("f5", "Biography")
        )

        val homeScreenItems = mutableListOf<HomeScreenItem>()
        homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Recently read", recentlyReadBooks)))
        homeScreenItems.add(HomeScreenItem.FilterRow(filters)) // Add filters here
        homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Best seller", bestSellerBooks)))
        homeScreenItems.add(HomeScreenItem.CategoryRow(Category("Books on sale", saleBooks)))
        // Add more sections as needed

        homeScreenAdapter.updateData(homeScreenItems)
    }
}