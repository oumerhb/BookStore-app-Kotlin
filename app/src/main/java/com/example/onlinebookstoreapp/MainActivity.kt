package com.example.onlinebookstoreapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.onlinebookstoreapp.auth.AuthManager
import com.example.onlinebookstoreapp.databinding.FragmentUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var authManager: AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(this)

        authManager = AuthManager.getInstance(this)
        setContentView(R.layout.activity_main)
        handleNavigationIntent()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerView, HomeFragment::class.java, null)
            }
        }

        // Set up bottom navigation
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.fragmentContainerView, HomeFragment::class.java, null)
                    }
                    true
                }
                R.id.nav_search -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.fragmentContainerView, SearchFragment::class.java, null)
                    }
                    true
                }
                R.id.nav_cart -> {
                    // Replace with CartFragment
                    supportFragmentManager.commit {
                        add(R.id.fragmentContainerView, CartFragment::class.java, null)
                    }
                    true
                }
                R.id.nav_profile -> {
                    handleProfileNavigation()
                    true
                }
                else -> false
            }
        }
    }

     fun handleNavigationIntent() {
        when {
            intent.getBooleanExtra("show_login", false) -> {
                // Navigate to login fragment
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_profile
                if (!authManager.isLoggedIn()) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.fragmentContainerView, LoginFragment::class.java, null)
                    }
                }
            }
            intent.getBooleanExtra("show_cart", false) -> {
                // Navigate to cart fragment
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_cart
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, CartFragment::class.java, null)
                }
            }
        }
    }

    fun handleProfileNavigation() {
        if (authManager.isLoggedIn()) {
            // User is logged in, show UserAccountFragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerView, UserAccountFragment::class.java, null)
            }
        } else {
            // User is not logged in, show LoginFragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerView, LoginFragment::class.java, null)
            }
        }
    }

    fun navigateToProfile() {
        // Update bottom navigation to show profile tab as selected
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_profile

        // Navigate to UserAccountFragment after successful login
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, UserAccountFragment::class.java, null)
        }
    }
    fun navigateToLogin() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, LoginFragment::class.java, null)
        }
    }

    fun navigateToRegister() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, RegisterFragment::class.java, null)
        }
    }
    fun navigateToSearchWithGenre(genre: String) {
        // Update bottom navigation to show search tab as selected
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_search

        // Replace current fragment with SearchFragment with prefiltered genre
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, SearchFragment.newInstance(genre))
        }
    }
}