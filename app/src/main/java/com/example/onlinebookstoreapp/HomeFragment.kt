package com.example.onlinebookstoreapp

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinebookstoreapp.databinding.FragmentHomeBinding
import com.example.onlinebookstoreapp.viewmodel.HomeViewModel
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var featuredAdapter: FeaturedBooksAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var newArrivalsAdapter: NewArrivalsAdapter
    //private val btnExplore=binding.btnExplore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnExplore=binding.btnExplore
        btnExplore.setOnClickListener {
            (activity as? MainActivity)?.navigateToSearchWithGenre("All Categories")
        }
        setupAdapters()
        setupObservers()
        viewModel.loadData()
    }

    private fun setupAdapters() {
        featuredAdapter = FeaturedBooksAdapter(emptyList()) { bookId ->
            // Navigate to book detail
            val intent = Intent(requireContext(), BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.EXTRA_BOOK_ID, bookId)
            startActivity(intent)
        }

        binding.featuredRecyclerView.apply {
            adapter = featuredAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecoration(8))
        }

        categoriesAdapter = CategoriesAdapter(emptyList()) { categoryName ->
            // Switch to search tab and pass prefiltered genre
            val bundle = Bundle().apply {
                putString("prefiltered_genre", categoryName)
            }

            // If using bottom navigation
            (activity as? MainActivity)?.navigateToSearchWithGenre(categoryName)
        }
        binding.categoriesGridView.adapter = categoriesAdapter

        newArrivalsAdapter = NewArrivalsAdapter(emptyList()) { bookId ->
            // Navigate to book detail
            val intent = Intent(requireContext(), BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.EXTRA_BOOK_ID, bookId)
            startActivity(intent)
        }
        binding.newArrivalsRecyclerView.adapter = newArrivalsAdapter
    }
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.featuredBooks.collect { books ->
                        featuredAdapter.updateBooks(books)
                    }
                }
                launch {
                    viewModel.categories.collect { categories ->
                        categoriesAdapter.updateCategories(categories)
                    }
                }
                launch {
                    viewModel.newArrivals.collect { books ->
                        newArrivalsAdapter.updateBooks(books)
                    }
                }
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    viewModel.error.collect { error ->
                        error?.let {
                            Log.d("homefragment",it)
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    class FeaturedBooksAdapter( private var books: List<BookEntity>,
                                private val onBookClick: (String) -> Unit ) :
        RecyclerView.Adapter<FeaturedBooksAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.featuredBookImage)
            val title: TextView = itemView.findViewById(R.id.featuredBookTitle)
            val author: TextView = itemView.findViewById(R.id.featuredBookAuthor)
            val rating: RatingBar = itemView.findViewById(R.id.featuredBookRating)
            val price: TextView = itemView.findViewById(R.id.featuredBookPrice)
        }

        fun updateBooks(newBooks: List<BookEntity>) {
            books = newBooks
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.featured_books, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val book = books[position]

            Glide.with(holder.itemView.context)
                .load(book.imageUrl)
                .placeholder(R.drawable.book)
                .error(R.drawable.book)
                .centerCrop()
                .into(holder.image)
            holder.title.text = book.title
            holder.author.text = book.author
            holder.rating.rating = book.rating
            holder.price.text = "ETB ${"%.2f".format(book.price)}"


            holder.itemView.setOnClickListener {
                onBookClick(book.id)
            }
        }

        override fun getItemCount() = books.size
    }

    class CategoriesAdapter( private var categories: List<CategoryEntity>,
                             private val onCategoryClick: (String) -> Unit ) : BaseAdapter() {

        override fun getCount() = categories.size
        override fun getItem(position: Int) = categories[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.books_category, parent, false)

            val category = categories[position]
            view.findViewById<TextView>(R.id.categoryName).text = category.name
            view.findViewById<TextView>(R.id.categoryCount).text = "${category.bookCount} books"
            // Load image with Coil/Glide: view.findViewById<ImageView>(R.id.categoryIcon).load(category.imageUrl)

            view.setOnClickListener {
                onCategoryClick(category.name)
            }

            return view
        }

        fun updateCategories(newCategories: List<CategoryEntity>) {
            categories = newCategories
            notifyDataSetChanged()
        }
    }

    class NewArrivalsAdapter( private var books: List<BookEntity>,
                              private val onBookClick: (String) -> Unit) :
        RecyclerView.Adapter<NewArrivalsAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.newArrivalImage)
            val title: TextView = itemView.findViewById(R.id.newArrivalTitle)
            val author: TextView = itemView.findViewById(R.id.newArrivalAuthor)
            val rating: RatingBar = itemView.findViewById(R.id.newArrivalRating)
            val price: TextView = itemView.findViewById(R.id.newArrivalPrice)
        }

        fun updateBooks(newBooks: List<BookEntity>) {
            books = newBooks
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.new_arrivals, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val book = books[position]

            Glide.with(holder.itemView.context)
                .load(book.imageUrl)
                .placeholder(R.drawable.book)
                .error(R.drawable.book)
                .centerCrop()
                .into(holder.image)
            holder.title.text = book.title
            holder.author.text = book.author
            holder.rating.rating = book.rating
            holder.price.text = "ETB ${"%.2f".format(book.price)}"

            holder.itemView.setOnClickListener {
                onBookClick(book.id)
                  }
        }

        override fun getItemCount() = books.size
    }
}

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