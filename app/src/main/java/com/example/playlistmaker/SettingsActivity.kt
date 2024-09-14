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

        val imageShare = findViewById<ImageView>(R.id.image_share)
        imageShare.setOnClickListener {
            val message = "https://practicum.yandex.ru/android-developer/?from=catalog"
            val shareIntent = Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, "Выберите приложение"))
        }

        val imageSupport = findViewById<ImageView>(R.id.image_support)
        imageSupport.setOnClickListener {
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val theme = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply { type = "text/plain" }
            shareIntent.setData(Uri.parse("mailto:diyavolik007@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, theme)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val imageUserAgreement = findViewById<ImageView>(R.id.image_user_agreement)
        imageUserAgreement.setOnClickListener {
            val url: Uri = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val openLink = Intent(Intent.ACTION_VIEW, url)
            startActivity(openLink)
        }
        }
    }

