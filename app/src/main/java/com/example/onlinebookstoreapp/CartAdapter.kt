package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinebookstoreapp.databinding.ItemCartProductBinding

class CartAdapter(
    var items: MutableList<CartFragment.CartItemEntity>,
    private val onQuantityChanged: (Int, Int) -> Unit,
    private val onRemoveItem: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartFragment.CartItemEntity) {
            // Update to use book-specific fields from the API
            binding.productName.text = item.title
            binding.productPrice.text = "${"%.2f".format(item.price)}"
            binding.productQuantity.text = item.quantity.toString()

            // Load book cover image if available
            if (binding.productImage != null) {
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.book)
                    .error(R.drawable.book)
                    .into(binding.productImage)
            }

            binding.increaseQuantity.setOnClickListener {
                val newQuantity = item.quantity + 1
                onQuantityChanged(adapterPosition, newQuantity)
            }

            binding.decreaseQuantity.setOnClickListener {
                val newQuantity = item.quantity - 1
                onQuantityChanged(adapterPosition, newQuantity)
            }

            binding.removeButton.setOnClickListener {
                onRemoveItem(adapterPosition)
            }
        }
    }

    fun updateItems(newItems: List<CartFragment.CartItemEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}