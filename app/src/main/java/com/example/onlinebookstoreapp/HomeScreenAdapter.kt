package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeScreenAdapter(
    private var items: List<HomeScreenItem>,
    private val onFilterClicked: (FilterOption) -> Unit,
    private val onSeeAllClicked: (String) -> Unit,
    private val onBookClicked: (Book) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_FILTER = 1
        private const val TYPE_CATEGORY_GRID = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeScreenItem.CategoryRow -> TYPE_CATEGORY
            is HomeScreenItem.FilterRow -> TYPE_FILTER
            is HomeScreenItem.CategoryGridRow -> TYPE_CATEGORY_GRID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category_row, parent, false)
                CategoryHorizontalViewHolder(view)
            }
            TYPE_FILTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_filter_row, parent, false)
                FilterViewHolder(view)
            }
            TYPE_CATEGORY_GRID -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category_grid_row, parent, false)
                CategoryGridViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is HomeScreenItem.CategoryRow -> (holder as CategoryHorizontalViewHolder).bind(currentItem.category,onBookClicked)
            is HomeScreenItem.FilterRow -> (holder as FilterViewHolder).bind(currentItem)
            is HomeScreenItem.CategoryGridRow -> (holder as CategoryGridViewHolder).bind(currentItem.category, onBookClicked)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HomeScreenItem>) {
        items = newItems
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    // --- ViewHolders ---

    inner class CategoryHorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitleTextView: TextView = itemView.findViewById(R.id.tv_category_title)
        private val seeAllTextView: TextView = itemView.findViewById(R.id.tv_see_all)
        private val booksRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_horizontal_books)

        fun bind(category: Category, onItemClicked: (Book) -> Unit) { // Pass onBookClicked
            categoryTitleTextView.text = category.title
            booksRecyclerView.apply {
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter(category.books, onItemClicked) // Pass to BookAdapter
            }
            seeAllTextView.setOnClickListener {
                onSeeAllClicked(category.title)
            }
        }
    }

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val filterTitleTextView: TextView = itemView.findViewById(R.id.tv_filter_title)
        private val chipGroup: ChipGroup = itemView.findViewById(R.id.chip_group_filters)

        fun bind(filterRow: HomeScreenItem.FilterRow) {
            filterTitleTextView.text = filterRow.title
            chipGroup.removeAllViews() // Clear old chips
            filterRow.filters.forEach { filterOption ->
                val chip = Chip(itemView.context).apply {
                    text = filterOption.text
                    isClickable = true
                    isCheckable = false // Or true if you want toggle behavior handled by Chip
                    // Add the small '>' icon as per wireframe
                    chipIcon = androidx.core.content.ContextCompat.getDrawable(itemView.context, R.drawable.ic_chevron_right_filter)
                    setIconStartPaddingResource(R.dimen.chip_icon_start_padding) // Define these if needed
                    setIconEndPaddingResource(R.dimen.chip_icon_end_padding)
                    // Style it to look more like the wireframe buttons
                    setChipBackgroundColorResource(R.color.chip_filter_background) // e.g. #F0F0F0
                    setTextColor(androidx.core.content.ContextCompat.getColor(itemView.context, R.color.chip_filter_text_color)) // e.g. #333333
                    // Set specific corner radius if needed
                    // shapeAppearanceModel = shapeAppearanceModel.withCornerSize(16f) // example
                }
                chip.setOnClickListener {
                    onFilterClicked(filterOption)
                }
                chipGroup.addView(chip)
            }
        }
    }
    inner class CategoryGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitleTextView: TextView = itemView.findViewById(R.id.tv_category_title_grid)
        private val seeAllTextView: TextView = itemView.findViewById(R.id.tv_see_all_grid)
        private val booksGridRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_grid_books)

        fun bind(category: Category, onItemClicked: (Book) -> Unit) { // Pass onBookClicked
            categoryTitleTextView.text = category.title
            // The GridLayoutManager is already set in XML, but you can also set/modify it here
            // booksGridRecyclerView.layoutManager = GridLayoutManager(itemView.context, 2) // Example: 2 columns
            booksGridRecyclerView.adapter = BookAdapter(category.books, onItemClicked) // Pass to BookAdapter

            seeAllTextView.setOnClickListener {
                onSeeAllClicked(category.title) // Or a different action for library "view all"
            }
        }
    }
}