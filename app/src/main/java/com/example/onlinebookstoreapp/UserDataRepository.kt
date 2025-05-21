// UserDataRepository.kt
package com.example.onlinebookstoreapp

object UserDataRepository {
    private val wishlistBookIds = mutableSetOf<String>()
    private val cartItems = mutableMapOf<String, Int>() // Key: Book ID, Value: Quantity

    // --- Wishlist Methods ---
    fun addToWishlist(bookId: String) {
        wishlistBookIds.add(bookId)
    }

    fun removeFromWishlist(bookId: String) {
        wishlistBookIds.remove(bookId)
    }

    fun isInWishlist(bookId: String): Boolean {
        return wishlistBookIds.contains(bookId)
    }

    fun getWishlistBookIds(): Set<String> {
        return wishlistBookIds.toSet() // Return a copy to prevent external modification
    }

    // --- Cart Methods ---
    fun addToCart(bookId: String, quantity: Int) {
        val currentQuantity = cartItems.getOrDefault(bookId, 0)
        cartItems[bookId] = currentQuantity + quantity
        // In a real app, you might have a max quantity or other checks
    }

     fun removeFromCart(bookId: String) {
        cartItems.remove(bookId)
    }

    fun updateCartItemQuantity(bookId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(bookId)
        } else {
            cartItems[bookId] = newQuantity
        }
    }
    fun getTotalCartPrice(): Double {
        var total = 0.0
        cartItems.forEach { (bookId, quantity) ->
            val book = BookRepository.getBookById(bookId)
            book?.let {
                total += it.getNumericPrice() * quantity
            }
        }
        return total
    }

    fun getCartItems(): Map<String, Int> {
        return cartItems.toMap() // Return a copy
    }

    fun getCartItemCount(): Int {
        return cartItems.values.sum()
    }

    fun clearCart() {
        cartItems.clear()
    }

    // You might add methods to calculate total cart price here
    // fun getTotalCartPrice(bookSource: (String) -> Book?): Double { ... }
}