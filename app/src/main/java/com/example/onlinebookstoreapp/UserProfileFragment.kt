package com.example.onlinebookstoreapp


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.onlinebookstoreapp.databinding.FragmentUserProfileBinding


class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // Important for toolbar menu in fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)

// Re-apply manual styling
        binding.toolbar.setBackgroundColor(resources.getColor(R.color.light_violet, null))
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white, null))
        binding.toolbar.navigationIcon?.setTint(resources.getColor(R.color.white, null))


        drawerToggle = ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Needed to show hamburger
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setHomeButtonEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            // Handle clicks later
            true
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Make toggle respond to clicks
        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
