package com.example.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.application.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset", "QueryPermissionsNeeded")

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.getViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSettings)
        binding.toolbarSettings.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.shareTheApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openTerms()
        }

//        binding.themeSwitcher.isChecked = viewModel.getTheme()



        binding.themeSwitcher.isChecked = viewModel.getTheme()

        Log.d("theme_isChecked", "проверили тему кнопки")
        Log.d("theme_isChecked_1", "тема кнопки ${binding.themeSwitcher.isChecked}")

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.getSwitchTheme(checked)
            recreate()
        }
        Log.d("theme_recreate", "пересоздали/создали")


    }
}

