package com.example.onlinebookstoreapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.example.onlinebookstoreapp.databinding.FragmentUserProfileBinding

class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
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
            Snackbar.make(binding.root, "Logout clicked", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadUserData() {
        // In a real app, you would load this from your data source
        binding.userName.text = "John Doe"
        binding.userEmail.text = "john.doe@example.com"
        // binding.profileImage.setImageResource(...)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}