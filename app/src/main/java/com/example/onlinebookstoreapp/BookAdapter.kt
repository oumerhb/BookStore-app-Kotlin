package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.databinding.ItemBookBinding


class BookAdapter(
    private val onBookClick: (BookEntity) -> Unit
) : ListAdapter<BookEntity, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookEntity) {
            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                tvPrice.text = "$${String.format("%.2f", book.price)}"
                tvDescription.text = book.description


                /* Handle genres display
                if (book.genres?.isNotEmpty() == true) {
                    tvGenres.text = book.genres.joinToString(", ")
                    tvGenres.visibility = android.view.View.VISIBLE
                } else {
                    tvGenres.visibility = android.view.View.GONE
                }*/

                // Set click listener
                root.setOnClickListener {
                    onBookClick(book)
                }
            }
        }
    }

    private class BookDiffCallback : DiffUtil.ItemCallback<BookEntity>() {
        override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem == newItem
        }
    }
}
