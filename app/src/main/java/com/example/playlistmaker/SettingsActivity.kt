package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button_settings)
        backButton.setOnClickListener {
            finish()
        }

        val imageShare = findViewById<ImageView>(R.id.image_share)
        imageShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_settings))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.select_app)))
        }

        val imageSupport = findViewById<ImageView>(R.id.image_support)
        imageSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply { type = "text/plain" }
            shareIntent.setData(Uri.parse(getString(R.string.uri_support_setting)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support_settings))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_support_settings))
            startActivity(shareIntent)
        }

        val imageUserAgreement = findViewById<ImageView>(R.id.image_user_agreement)
        imageUserAgreement.setOnClickListener {
            val url: Uri = Uri.parse(getString(R.string.uri_agreement_setting))
            val openLink = Intent(Intent.ACTION_VIEW, url)
            startActivity(openLink)
        }
    }
}

