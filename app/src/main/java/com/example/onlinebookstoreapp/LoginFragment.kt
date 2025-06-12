package com.example.onlinebookstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        authManager = AuthManager.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // Add click listener for the "Create Account" button
        binding.btnRegister.setOnClickListener {
            (activity as? MainActivity)?.navigateToRegister()
        }

        // Add click listener for the "Sign up" text
        binding.tvSignUp.setOnClickListener {
            (activity as? MainActivity)?.navigateToRegister()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.editText?.text.toString().trim()
        val password = binding.etPassword.editText?.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val loginRequest = mapOf(
                    "email" to email,
                    "password" to password
                )

                val response = apiService.login(loginRequest)

                // Save auth token and user info
                authManager.saveAuthToken(response.data.accessToken.token)
                authManager.saveUserInfo(response.data.user.name, response.data.user.email)

                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

                // Navigate to UserAccountFragment
                (activity as? MainActivity)?.navigateToProfile()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}