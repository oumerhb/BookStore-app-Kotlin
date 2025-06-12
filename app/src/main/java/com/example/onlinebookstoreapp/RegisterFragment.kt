package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.databinding.FragmentRegisterBinding // You'll create this layout
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val apiService: BookstoreApiService by lazy {
        RetrofitClient.apiService
    }

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        authManager = AuthManager.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            performRegistration()
        }

        binding.tvSignIn.setOnClickListener {
            // Navigate back to login fragment
            (activity as? MainActivity)?.navigateToLogin()
        }
    }

    private fun performRegistration() {
        val name = binding.etName.editText?.text.toString().trim()
        val email = binding.etEmail.editText?.text.toString().trim()
        val password = binding.etPassword.editText?.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val registerRequest = mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password
                )

                val response = apiService.register(registerRequest)

                // Registration successful, you might want to automatically log in the user
                // or navigate to login screen
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                // Optionally, log in the user after successful registration
                // For simplicity, we'll just navigate to login screen
                (activity as? MainActivity)?.navigateToLogin()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}