package com.example.playlistmaker.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> {
                    binding.divider.isVisible = false
                    binding.bottomNavigationView.isVisible = false
                }

                R.id.newPlayListFragment -> {
                    binding.divider.isVisible = false
                    binding.bottomNavigationView.isVisible = false
                }

                R.id.playListInfoFragment -> {
                    binding.divider.isVisible = false
                    binding.bottomNavigationView.isVisible = false
                }

                else -> {
                    binding.divider.isVisible = true
                    binding.bottomNavigationView.isVisible = true
                }
            }

        }


    }
}