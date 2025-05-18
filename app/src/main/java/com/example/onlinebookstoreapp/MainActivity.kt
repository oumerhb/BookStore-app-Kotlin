package com.example.onlinebookstoreapp

// MainActivity.kt (or your Fragment)
import android.content.Intent
import android.os.Bundle
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

                else -> false
            }
        }
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home // Select home by default
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

    private fun loadHomeData() {
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
    private fun loadLibraryData() {
        supportActionBar?.title = "My Library"

        // Sample data for library - in a real app, these would be user's purchased/saved books
        val libraryBooks = listOf(
            Book("dm", "Don't Make Me Think", "Steve Krug", category = "Web design", imageUrl = "dont_make_me_think_cover"),
            Book("dj", "Design is a Job", "Mike Monteiro", category = "Web design", imageUrl = "design_is_a_job_cover"),
            Book("dw", "Designing with Web Standards", "Jeffrey Zeldman", category = "Web design", imageUrl = "designing_with_web_standards_cover"),
            Book("s1", "JavaScript and JQuery", "Jon Duckett", category = "Web design", imageUrl = "js_jquery_cover"),
            Book("s2", "Responsive Web Design", "Ethan Marcotte", category = "Web design", imageUrl = "responsive_web_cover"),
            Book("s3", "Neuro Web Design", "Susan Weinschenk", category = "Web design", imageUrl = "neuro_web_cover"),
            Book("1", "The Midnight Library", "Matt Haig", category = "Romance", imageUrl = "placeholder_image_simple_x"),
            Book("2", "Klara and the Sun", "Kazuo Ishiguro", category = "Science", imageUrl = "placeholder_image_simple_x"),
            Book("4", "Atomic Habits", "James Clear", category = "Design", imageUrl = "placeholder_image_simple_x")
            // Add more books with different categories
        )

        val booksByCategory = libraryBooks.groupBy { it.category }
        val libraryScreenItems = mutableListOf<HomeScreenItem>()

        booksByCategory.forEach { (categoryName, booksInCategory) ->
            libraryScreenItems.add(HomeScreenItem.CategoryGridRow(Category(categoryName, booksInCategory)))
        }

        if (libraryScreenItems.isEmpty()) {
            // You can add a placeholder item for an empty library
            // e.g., a TextView in a custom layout
            homeScreenAdapter.updateData(listOf(HomeScreenItem.CategoryGridRow(Category("Your Library is Empty", emptyList()))))

        } else {
            homeScreenAdapter.updateData(libraryScreenItems)
        }
    }
}