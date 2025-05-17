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
    private val onSeeAllClicked: (String) -> Unit // Category title
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_FILTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeScreenItem.CategoryRow -> TYPE_CATEGORY
            is HomeScreenItem.FilterRow -> TYPE_FILTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category_row, parent, false)
                CategoryViewHolder(view)
            }
            TYPE_FILTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_filter_row, parent, false)
                FilterViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is HomeScreenItem.CategoryRow -> (holder as CategoryViewHolder).bind(currentItem.category)
            is HomeScreenItem.FilterRow -> (holder as FilterViewHolder).bind(currentItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HomeScreenItem>) {
        items = newItems
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    // --- ViewHolders ---

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitleTextView: TextView = itemView.findViewById(R.id.tv_category_title)
        private val seeAllTextView: TextView = itemView.findViewById(R.id.tv_see_all)
        private val booksRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_horizontal_books)

        fun bind(category: Category) {
            categoryTitleTextView.text = category.title
            booksRecyclerView.apply {
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter(category.books)
                // For performance, if item sizes are fixed:
                // setHasFixedSize(true)
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
}