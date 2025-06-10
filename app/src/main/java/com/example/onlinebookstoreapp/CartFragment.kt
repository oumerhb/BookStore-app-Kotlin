package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.onlinebookstoreapp.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter

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
        updateCartUI()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            items = getSampleCartItems(),
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
            Snackbar.make(binding.root, "Proceeding to checkout", Snackbar.LENGTH_SHORT).show()
            // Navigate to checkout screen
        }

        binding.startShoppingButton.setOnClickListener {
            Snackbar.make(binding.root, "Start shopping clicked", Snackbar.LENGTH_SHORT).show()
            // Navigate to products screen
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
        val discount = 10.00 // Sample discount
        val total = subtotal + shipping + tax - discount

        binding.subtotalPrice.text = "$${"%.2f".format(subtotal)}"
        binding.shippingPrice.text = "$${"%.2f".format(shipping)}"
        binding.taxPrice.text = "$${"%.2f".format(tax)}"
        binding.discountPrice.text = "-$${"%.2f".format(discount)}"
        binding.totalPrice.text = "$${"%.2f".format(total)}"
    }

    private fun updateItemQuantity(position: Int, newQuantity: Int) {
        if (newQuantity > 0) {
            cartAdapter.items[position].quantity = newQuantity
            cartAdapter.notifyItemChanged(position)
            updateOrderSummary()
        } else {
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        cartAdapter.items.removeAt(position)
        cartAdapter.notifyItemRemoved(position)
        updateCartUI()
        Snackbar.make(binding.root, "Item removed from cart", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                // Implement undo logic if needed
            }
            .show()
    }

    private fun applyCoupon(couponCode: String) {
        // In a real app, you would validate the coupon with your backend
        Snackbar.make(binding.root, "Coupon applied: $couponCode", Snackbar.LENGTH_SHORT).show()
        // Update discount and order summary
        binding.discountPrice.text = "-$${"%.2f".format(15.00)}" // Increased discount
        updateOrderSummary()
    }

    private fun getSampleCartItems(): MutableList<CartItem> {
        return mutableListOf(
            CartItem(
                id = "1",
                name = "Premium Wireless Headphones",
                variant = "Color: Black | Size: One Size",
                price = 59.99,
                quantity = 2,
                imageUrl = ""
            ),
            CartItem(
                id = "2",
                name = "Organic Cotton T-Shirt",
                variant = "Color: White | Size: M",
                price = 24.99,
                quantity = 1,
                imageUrl = ""
            ),
            CartItem(
                id = "3",
                name = "Stainless Steel Water Bottle",
                variant = "Color: Silver | Capacity: 750ml",
                price = 19.99,
                quantity = 3,
                imageUrl = ""
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class CartItem(
        val id: String,
        val name: String,
        val variant: String,
        val price: Double,
        var quantity: Int,
        val imageUrl: String
    )
}