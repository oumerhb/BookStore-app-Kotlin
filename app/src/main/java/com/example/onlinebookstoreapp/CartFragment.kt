package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.onlinebookstoreapp.databinding.FragmentCartBinding
import com.example.onlinebookstoreapp.model.AddToCartRequest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter

    private val apiService: BookstoreApiService by lazy {
        RetrofitClient.apiService
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        loadCartFromAPI()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            items = mutableListOf(),
            onQuantityChanged = { position, newQuantity ->
                updateItemQuantity(position, newQuantity)
            },
            onRemoveItem = { position ->
                removeItem(position)
            }
        )

        binding.cartItemsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.applyCouponButton.setOnClickListener {
            val couponCode = binding.couponCodeInput.text.toString().trim()
            if (couponCode.isNotEmpty()) {
                applyCoupon(couponCode)
            } else {
                Snackbar.make(binding.root, "Please enter a coupon code", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.checkoutButton.setOnClickListener {
            proceedToCheckout()
        }

        binding.startShoppingButton.setOnClickListener {
            // Navigate back to home or search
            (activity as? MainActivity)?.navigateToSearchWithGenre("All Categories")
        }
    }

    private fun loadCartFromAPI() {
        binding.progressBar?.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = apiService.getCartItems()
                val cartItems = response.data.cart.items.map { cartItem ->
                    CartItemEntity(
                        id = cartItem.book.id,
                        bookId = cartItem.book.id,
                        title = cartItem.book.title,
                        author = cartItem.book.author,
                        price = cartItem.book.price,
                        quantity = cartItem.quantity,
                        imageUrl = cartItem.book.coverImage ?: ""
                    )
                }

                cartAdapter.updateItems(cartItems)
                updateCartUI()

            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error loading cart: ${e.message}", Snackbar.LENGTH_LONG).show()
                updateCartUI() // Show empty state
            } finally {
                binding.progressBar?.visibility = View.GONE
            }
        }
    }

    private fun updateItemQuantity(position: Int, newQuantity: Int) {
        val cartItem = cartAdapter.items[position]

        if (newQuantity > 0) {
            lifecycleScope.launch {
                try {
                    val updateRequest = mapOf(
                        "items" to listOf(
                            mapOf(
                                "bookId" to cartItem.bookId,
                                "quantity" to newQuantity
                            )
                        )
                    )

                    apiService.updateCartQuantities(updateRequest)
                    cartItem.quantity = newQuantity
                    cartAdapter.notifyItemChanged(position)
                    updateOrderSummary()

                } catch (e: Exception) {
                    Snackbar.make(binding.root, "Error updating quantity: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        val cartItem = cartAdapter.items[position]

        lifecycleScope.launch {
            try {
                apiService.removeBookFromCart(cartItem.bookId)
                cartAdapter.items.removeAt(position)
                cartAdapter.notifyItemRemoved(position)
                updateCartUI()

                Snackbar.make(binding.root, "Item removed from cart", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        // Re-add item to cart
                        addItemBackToCart(cartItem)
                    }
                    .show()

            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error removing item: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addItemBackToCart(cartItem: CartItemEntity) {
        lifecycleScope.launch {
            try {
                val addRequest = AddToCartRequest(
                    bookId = cartItem.bookId,
                    quantity = cartItem.quantity
                )

                apiService.addToCart(addRequest)
                loadCartFromAPI() // Reload cart to get updated data

            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error adding item back: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCartUI() {
        val cartItems = cartAdapter.items
        val isEmpty = cartItems.isEmpty()

        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.nestedScrollView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.checkoutButton.visibility = if (isEmpty) View.GONE else View.VISIBLE

        if (!isEmpty) {
            updateOrderSummary()
        }
    }

    private fun updateOrderSummary() {
        val subtotal = cartAdapter.items.sumOf { it.price * it.quantity }
        val shipping = 5.99
        val tax = subtotal * 0.105 // 10.5% tax
        val discount = 0.00 // No discount by default
        val total = subtotal + shipping + tax - discount

        binding.subtotalPrice.text = "$${"%.2f".format(subtotal)}"
        binding.shippingPrice.text = "$${"%.2f".format(shipping)}"
        binding.taxPrice.text = "$${"%.2f".format(tax)}"
        binding.discountPrice.text = "-$${"%.2f".format(discount)}"
        binding.totalPrice.text = "$${"%.2f".format(total)}"
    }

    private fun applyCoupon(couponCode: String) {
        // For now, just show a message since coupon API isn't implemented
        Snackbar.make(binding.root, "Coupon functionality not yet implemented", Snackbar.LENGTH_SHORT).show()
    }

    private fun proceedToCheckout() {
        if (cartAdapter.items.isNotEmpty()) {
            // Navigate to checkout or create order
            Snackbar.make(binding.root, "Proceeding to checkout...", Snackbar.LENGTH_SHORT).show()
            // You can implement order creation here using the order API
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Updated data class to match API response
    data class CartItemEntity(
        val id: String,
        val bookId: String,
        val title: String,
        val author: String,
        val price: Double,
        var quantity: Int,
        val imageUrl: String
    )
}