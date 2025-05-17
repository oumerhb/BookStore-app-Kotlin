package com.example.onlinebookstoreapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// import com.bumptech.glide.Glide // If using Glide for image loading

class BookAdapter(private val books: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_card, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverImageView: ImageView = itemView.findViewById(R.id.iv_book_cover)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.tv_book_author)
        private val actionButton: Button = itemView.findViewById(R.id.btn_book_action_placeholder)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            actionButton.text = book.price ?: "View" // Example

            // For image loading (using Glide example, replace with your choice)
            // if (book.imageUrl != null) {
            //     Glide.with(itemView.context).load(book.imageUrl).into(coverImageView)
            // } else {
            //     coverImageView.setImageResource(R.drawable.placeholder_book_cover) // a default placeholder
            // }
            // For wireframe, we can just leave the background or set a placeholder
            coverImageView.setImageResource(R.drawable.placeholder_book_cover_with_x) // create this
        }
    }
}