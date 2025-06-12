package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.databinding.FragmentUserProfileBinding
import com.google.android.material.snackbar.Snackbar

class UserAccountFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        authManager = AuthManager.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        loadUserData()
    }

    private fun setupClickListeners() {
        binding.ordersItem.setOnClickListener {
            Snackbar.make(binding.root, "Orders clicked", Snackbar.LENGTH_SHORT).show()
        }

        binding.addressesItem.setOnClickListener {
            Snackbar.make(binding.root, "Addresses clicked", Snackbar.LENGTH_SHORT).show()
        }

        binding.settingsItem.setOnClickListener {
            Snackbar.make(binding.root, "Settings clicked", Snackbar.LENGTH_SHORT).show()
        }

        binding.helpItem.setOnClickListener {
            Snackbar.make(binding.root, "Help clicked", Snackbar.LENGTH_SHORT).show()
        }

        binding.logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun loadUserData() {
        // Load user data from AuthManager
        binding.userName.text = authManager.getUserName() ?: "Unknown User"
        binding.userEmail.text = authManager.getUserEmail() ?: "No email"
    }

    private fun performLogout() {
        authManager.logout()
        Snackbar.make(binding.root, "Logged out successfully", Snackbar.LENGTH_SHORT).show()

        // Navigate back to login
        (activity as? MainActivity)?.handleProfileNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}