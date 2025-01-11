package com.example.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.application.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset")
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
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply { type = "text/plain" }
            shareIntent.setData(Uri.parse(getString(R.string.uri_support_setting)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support_settings))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_support_settings))
            startActivity(shareIntent)
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

