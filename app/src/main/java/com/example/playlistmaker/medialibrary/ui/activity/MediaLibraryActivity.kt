package com.example.playlistmaker.medialibrary.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.medialibrary.ui.fragment.MediaLibraryPagerAdapter
import com.example.playlistmaker.medialibrary.ui.viewmodel.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediaLibraryActivity : AppCompatActivity(R.layout.activity_media_library) {

    private val viewModel: MediaLibraryViewModel by viewModel()

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMediaLibrary)
        binding.toolbarMediaLibrary.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarMediaLibrary.setNavigationOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaLibraryPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when(position){
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
