package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeScreenAdapter(
    private var items: List<HomeScreenItem>,
    private val onFilterClicked: (FilterOption) -> Unit,
    private val onSeeAllClicked: (String) -> Unit,
    private val onBookClicked: (Book) -> Unit,
    private val onRemoveCartItemClicked: (bookId: String) -> Unit,
    private val onUpdateCartItemQuantity: (bookId: String, newQuantity: Int) -> Unit,
    private val onCheckoutClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_FILTER = 1
        private const val TYPE_CATEGORY_GRID = 2
        private const val TYPE_CART_ITEM = 3
        private const val TYPE_CART_SUMMARY = 4
        private const val TYPE_EMPTY_STATE = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeScreenItem.CategoryRow -> TYPE_CATEGORY
            is HomeScreenItem.FilterRow -> TYPE_FILTER
            is HomeScreenItem.CategoryGridRow -> TYPE_CATEGORY_GRID
            is HomeScreenItem.CartItemEntry -> TYPE_CART_ITEM
            is HomeScreenItem.CartSummary -> TYPE_CART_SUMMARY
            is HomeScreenItem.EmptyStateItem -> TYPE_EMPTY_STATE
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
            TYPE_CART_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cart_book, parent, false)
                CartItemViewHolder(view)
            }
            TYPE_CART_SUMMARY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cart_summary, parent, false)
                CartSummaryViewHolder(view)
            }
            TYPE_EMPTY_STATE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty_state, parent, false)
                EmptyStateViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is HomeScreenItem.CategoryRow -> (holder as CategoryHorizontalViewHolder).bind(currentItem.category,onBookClicked)
            is HomeScreenItem.FilterRow -> (holder as FilterViewHolder).bind(currentItem)
            is HomeScreenItem.CategoryGridRow -> (holder as CategoryGridViewHolder).bind(currentItem.category, onBookClicked)
            is HomeScreenItem.CartItemEntry -> (holder as CartItemViewHolder).bind(currentItem)
            is HomeScreenItem.CartSummary -> (holder as CartSummaryViewHolder).bind(currentItem)
            is HomeScreenItem.EmptyStateItem -> (holder as EmptyStateViewHolder).bind(currentItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HomeScreenItem>) {
        items = newItems
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    // --- ViewHolders ---
    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverImageView: ImageView = itemView.findViewById(R.id.iv_cart_book_cover)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_cart_book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.tv_cart_book_author)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_cart_book_price)
        private val quantityTextView: TextView = itemView.findViewById(R.id.tv_cart_quantity)
        private val removeButton: ImageButton = itemView.findViewById(R.id.ib_cart_remove_item)
        private val plusButton: ImageButton = itemView.findViewById(R.id.ib_cart_quantity_plus)
        private val minusButton: ImageButton = itemView.findViewById(R.id.ib_cart_quantity_minus)

        fun bind(cartItem: HomeScreenItem.CartItemEntry) {
            val book = cartItem.book
            titleTextView.text = book.title
            authorTextView.text = book.author
            priceTextView.text = book.price ?: "N/A" // Or format book.getNumericPrice()
            quantityTextView.text = cartItem.quantity.toString()

            // Load image (Glide example)
            // Glide.with(itemView.context).load(book.imageUrl).placeholder(R.drawable.placeholder_image_simple_x).into(coverImageView)
            if (book.imageUrl != null && book.imageUrl.isNotBlank()) {
                val resId = itemView.context.resources.getIdentifier(book.imageUrl, "drawable", itemView.context.packageName)
                if (resId != 0) coverImageView.setImageResource(resId) else coverImageView.setImageResource(R.drawable.placeholder_book_cover_with_x)
            } else {
                coverImageView.setImageResource(R.drawable.placeholder_book_cover_with_x)
            }


            removeButton.setOnClickListener {
                onRemoveCartItemClicked(book.id)
            }
            plusButton.setOnClickListener {
                val newQuantity = cartItem.quantity + 1
                // No need to update cartItem.quantity here, MainActivity will reload data
                onUpdateCartItemQuantity(book.id, newQuantity)
            }
            minusButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    val newQuantity = cartItem.quantity - 1
                    onUpdateCartItemQuantity(book.id, newQuantity)
                } else {
                    // If quantity is 1 and minus is clicked, remove the item
                    onRemoveCartItemClicked(book.id)
                }
            }
        }
    }

    inner class CartSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subtotalPriceTextView: TextView = itemView.findViewById(R.id.tv_cart_subtotal_price)
        private val shippingPriceTextView: TextView = itemView.findViewById(R.id.tv_cart_shipping_price) // Assuming fixed shipping
        private val totalPriceTextView: TextView = itemView.findViewById(R.id.tv_cart_total_price)
        private val checkoutButton: Button = itemView.findViewById(R.id.btn_cart_checkout)

        fun bind(summary: HomeScreenItem.CartSummary) {
            val context = itemView.context
            val shippingCost = 25.0 // Example fixed shipping
            val subtotal = summary.totalPrice
            val total = subtotal + shippingCost

            subtotalPriceTextView.text = context.getString(R.string.price_format, subtotal) // "EGP %.2f"
            shippingPriceTextView.text = context.getString(R.string.price_format, shippingCost)
            totalPriceTextView.text = context.getString(R.string.price_format, total)

            checkoutButton.setOnClickListener {
                onCheckoutClicked()
            }
        }
    }
    inner class EmptyStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.iv_empty_state_icon)
        private val messageTextView: TextView = itemView.findViewById(R.id.tv_empty_state_message)

        fun bind(emptyState: HomeScreenItem.EmptyStateItem) {
            messageTextView.text = emptyState.message
            if (emptyState.iconResId != null) {
                iconImageView.setImageResource(emptyState.iconResId)
                iconImageView.visibility = View.VISIBLE
            } else {
                iconImageView.visibility = View.GONE
            }
        }
    }
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