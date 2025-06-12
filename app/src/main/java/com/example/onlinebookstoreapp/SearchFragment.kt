package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.example.onlinebookstoreapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter

    // Use the singleton RetrofitClient instead of creating a new instance
    private val apiService: BookstoreApiService by lazy {
        RetrofitClient.apiService
    }

    companion object {
        private const val ARG_PREFILTERED_GENRE = "prefiltered_genre"

        fun newInstance(prefilledGenre: String? = null): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            prefilledGenre?.let { args.putString(ARG_PREFILTERED_GENRE, it) }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = binding.toolbar
        toolbar.title = "Search Books"

        setupUI()
        setupRecyclerView()
        setupSearchListeners()

        // Handle prefiltered genre from arguments
        arguments?.getString(ARG_PREFILTERED_GENRE)?.let { genre ->
            binding.categorySpinner.setText(genre, false)
            performSearch() // Automatically search with the prefiltered genre
        }
    }

    private fun setupUI() {
        val categories = arrayOf(
            "All Categories", "Fiction", "Non Fiction", "Science Fiction",
            "Mystery", "Thriller", "Romance", "History", "Biography",
            "Self Help", "Programming", "Fantasy", "Horror", "Classic",
            "Poetry", "Young Adult", "Dystopia", "Historical Fiction",
            "Psychology", "Science", "Technology", "Philosophy"
        )

        binding.categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
        )
        binding.categorySpinner.setText("All Categories", false)

        binding.etMinPrice.setText("0.00")
        binding.etMaxPrice.setText("100.00")
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter { book ->
            // Handle book click - navigate to book details
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookAdapter
        }
    }

    private fun setupSearchListeners() {
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        binding.btnClearFilters?.setOnClickListener {
            clearFilters()
        }
    }

    private fun performSearch() {
        val searchQuery = binding.etSearchQuery.text.toString().trim()
        val selectedCategory = binding.categorySpinner.text.toString()
        val minPrice = binding.etMinPrice.text.toString().toDoubleOrNull()
        val maxPrice = binding.etMaxPrice.text.toString().toDoubleOrNull()

        val queryParams = mutableMapOf<String, String>().apply {
            if (searchQuery.isNotEmpty()) {
                put("search", searchQuery)
            }
            if (selectedCategory != "All Categories") {
                put("genres", selectedCategory)
            }
            minPrice?.let { put("priceMin", it.toString()) }
            maxPrice?.let { put("priceMax", it.toString()) }
            put("page", "1")
            put("limit", "20")
            put("sort", "title:asc")
        }

        searchBooks(queryParams)
    }

    private fun searchBooks(queryParams: Map<String, String>) {
        binding.progressBar?.visibility = View.VISIBLE
        binding.tvNoResults.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = apiService.searchBooks(queryParams)
                val bookEntities = response.data.books.map { book -> book.toEntity() }

                if (bookEntities.isNotEmpty()) {
                    bookAdapter.submitList(bookEntities)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.tvNoResults.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.tvNoResults.visibility = View.VISIBLE
                    binding.tvNoResults.text = "No books found matching your criteria"
                }
            } catch (e: Exception) {
                binding.recyclerView.visibility = View.GONE
                binding.tvNoResults.visibility = View.VISIBLE
                binding.tvNoResults.text = "Error searching books: ${e.message}"
            } finally {
                binding.progressBar?.visibility = View.GONE
            }
        }
    }

    private fun clearFilters() {
        binding.etSearchQuery.setText("")
        binding.categorySpinner.setText("All Categories", false)
        binding.etMinPrice.setText("0.00")
        binding.etMaxPrice.setText("100.00")

        binding.recyclerView.visibility = View.GONE
        binding.tvNoResults.visibility = View.VISIBLE
        binding.tvNoResults.text = "No search performed yet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}