package com.example.onlinebookstoreapp

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.RatingBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private lateinit var featuredRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var newArrivalsRecyclerView: RecyclerView

    private lateinit var featuredAdapter: FeaturedBooksAdapter
    private lateinit var newArrivalsAdapter: NewArrivalsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        featuredRecyclerView = view.findViewById(R.id.featuredRecyclerView)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        newArrivalsRecyclerView = view.findViewById(R.id.newArrivalsRecyclerView)

        setupFeaturedBooks()
        setupCategories()
        setupNewArrivals()
    }

    private fun setupFeaturedBooks() {
        featuredAdapter = FeaturedBooksAdapter(getFeaturedBooks())
        featuredRecyclerView.apply {
            adapter = featuredAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecoration(8))
        }
    }

    private fun setupCategories() {
        categoriesAdapter = CategoriesAdapter(getCategories())
        categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setupNewArrivals() {
        newArrivalsAdapter = NewArrivalsAdapter(getNewArrivals())
        newArrivalsRecyclerView.adapter = newArrivalsAdapter
    }

    private fun getFeaturedBooks(): List<Book> {
        // Replace with your actual data source
        return listOf(
            Book("The Silent Patient", "Alex Michaelides", 4.5f, 124, 12.99, R.drawable.book),
            Book("Where the Crawdads Sing", "Delia Owens", 4.7f, 89, 14.99, R.drawable.book),
            Book("Educated", "Tara Westover", 4.8f, 76, 10.99, R.drawable.book),
            Book("Becoming", "Michelle Obama", 4.9f, 210, 15.99, R.drawable.book),
            Book("The Testaments", "Margaret Atwood", 4.3f, 67, 13.99, R.drawable.book)
        )
    }

    private fun getCategories(): List<Category> {
        // Replace with your actual data source
        return listOf(
            Category("Fiction", 124, R.drawable.book),
            Category("Mystery", 87, R.drawable.book),
            Category("Romance", 92, R.drawable.book),
            Category("Sci-Fi", 56, R.drawable.book),
            Category("Biography", 43, R.drawable.book),
            Category("History", 38, R.drawable.book),
            Category("Self-Help", 65, R.drawable.book),
            Category("Business", 41, R.drawable.book)
        )
    }

    private fun getNewArrivals(): List<Book> {
        // Replace with your actual data source
        return listOf(
            Book("The Midnight Library", "Matt Haig", 4.6f, 42, 12.99, R.drawable.book),
            Book("Project Hail Mary", "Andy Weir", 4.8f, 37, 15.99, R.drawable.book),
            Book("Klara and the Sun", "Kazuo Ishiguro", 4.4f, 28, 13.99, R.drawable.book),
            Book("The Push", "Ashley Audrain", 4.2f, 31, 11.99, R.drawable.book),
            Book("No One Is Talking About This", "Patricia Lockwood", 4.1f, 19, 10.99, R.drawable.book),
            Book("The Four Winds", "Kristin Hannah", 4.7f, 53, 14.99, R.drawable.book)
        )
    }
}

// Adapter classes
class FeaturedBooksAdapter(private val books: List<Book>) :
    RecyclerView.Adapter<FeaturedBooksAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.featuredBookImage)
        val title: TextView = itemView.findViewById(R.id.featuredBookTitle)
        val author: TextView = itemView.findViewById(R.id.featuredBookAuthor)
        val rating: RatingBar = itemView.findViewById(R.id.featuredBookRating)
        val price: TextView = itemView.findViewById(R.id.featuredBookPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.featured_books, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        holder.image.setImageResource(book.imageRes)
        holder.title.text = book.title
        holder.author.text = book.author
        holder.rating.rating = book.rating
        holder.price.text = "$${book.price}"

        holder.itemView.setOnClickListener {
            // Handle book click
        }
    }

    override fun getItemCount() = books.size
}

class CategoriesAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById<TextView>(R.id.categoryName)
        val image: ImageView = itemView.findViewById<ImageView>(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.books_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = categories[position]
        holder.textView.text = cat.name
        holder.image.setImageResource(cat.iconRes)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}

class NewArrivalsAdapter(private val books: List<Book>) :
    RecyclerView.Adapter<NewArrivalsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.newArrivalImage)
        val title: TextView = itemView.findViewById(R.id.newArrivalTitle)
        val author: TextView = itemView.findViewById(R.id.newArrivalAuthor)
        val rating: RatingBar = itemView.findViewById(R.id.newArrivalRating)
        val price: TextView = itemView.findViewById(R.id.newArrivalPrice)
        val originalPrice: TextView = itemView.findViewById(R.id.newArrivalOriginalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_arrivals, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        holder.image.setImageResource(book.imageRes)
        holder.title.text = book.title
        holder.author.text = book.author
        holder.rating.rating = book.rating
        holder.price.text = "$${book.price}"

        holder.itemView.setOnClickListener {
            // Handle book click
        }
    }

    override fun getItemCount() = books.size
}

// Data classes
data class Book(
    val title: String,
    val author: String,
    val rating: Float,
    val reviewCount: Int,
    val price: Double,
    val imageRes: Int
)

data class Category(
    val name: String,
    val count: Int,
    val iconRes: Int
)

// Item decoration for spacing in RecyclerView
class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacing
        outRect.right = spacing
    }
}