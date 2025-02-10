package com.example.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset", "QueryPermissionsNeeded")

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

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
            viewModel.shareApp(this)
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport(this)
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openTerms(this)
        }

        binding.themeSwitcher.isChecked = viewModel.getTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.getSwitchTheme(checked)
        }
       }
}

