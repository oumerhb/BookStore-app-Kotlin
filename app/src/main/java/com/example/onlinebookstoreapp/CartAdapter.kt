package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinebookstoreapp.databinding.ItemCartProductBinding

class CartAdapter(
    var items: MutableList<CartFragment.CartItem>,
    private val onQuantityChanged: (Int, Int) -> Unit,
    private val onRemoveItem: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartFragment.CartItem) {
            binding.productName.text = item.name
            binding.productVariant.text = item.variant
            binding.productPrice.text = "$${"%.2f".format(item.price)}"
            binding.productQuantity.text = item.quantity.toString()

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