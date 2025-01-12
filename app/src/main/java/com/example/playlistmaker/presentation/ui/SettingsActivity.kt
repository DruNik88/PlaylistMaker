package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.application.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val appInstance = (applicationContext as App)

        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val imageShare = findViewById<TextView>(R.id.share_the_app)
        imageShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_settings))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.select_app)))
        }

        val imageSupport = findViewById<TextView>(R.id.write_to_support)
        imageSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.uri_support_setting)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.theme_support_settings)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.message_support_settings)
                )
            }
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(shareIntent,getString(R.string.select_app)))
            } else {
                Toast.makeText(this, R.string.not_found_email_application, Toast.LENGTH_SHORT).show()
            }
        }

        val imageUserAgreement = findViewById<TextView>(R.id.user_agreement)
        imageUserAgreement.setOnClickListener {
            val url: Uri = Uri.parse(getString(R.string.uri_agreement_setting))
            val openLink = Intent(Intent.ACTION_VIEW, url)
            startActivity(openLink)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = appInstance.currentSwitchTheme()

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            appInstance.switchTheme(checked)
        }

        appInstance.saveTheme()
    }
}

