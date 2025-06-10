package com.example.onlinebookstoreapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.onlinebookstoreapp.databinding.FragmentUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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
                    // Replace with ProfileFragment
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add(R.id.fragmentContainerView, UserAccountFragment::class.java, null)
                    }
                    true
                }
                else -> false
            }
        }
    }
}