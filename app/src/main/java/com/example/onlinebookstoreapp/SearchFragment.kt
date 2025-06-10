package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.example.onlinebookstoreapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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

        // Properly set up the toolbar
        val toolbar: MaterialToolbar = binding.toolbar
        toolbar.title = "Search Books"

        // Set up the static UI
        setupStaticUI()
    }

    private fun setupStaticUI() {
        // Set up category spinner with sample data
        val categories = arrayOf("All Categories", "Fiction", "Non-Fiction", "Science", "History")
        binding.categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
        )
        binding.categorySpinner.setText("All Categories", false)

        // Set default price values
        binding.etMinPrice.setText("0.00")
        binding.etMaxPrice.setText("100.00")

        // Hide results section (only show filters)
        binding.recyclerView.visibility = View.GONE
        binding.tvNoResults.visibility = View.VISIBLE
        binding.tvNoResults.text = "No search performed yet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}