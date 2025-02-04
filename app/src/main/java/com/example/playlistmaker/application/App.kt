package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var getTheme: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        sharedPrefs = Creator.provideSharedPreferences()
        getTheme = Creator.provideGetSettingsInteractor(applicationContext)
        val theme = getTheme.getSettingTheme()
        when (theme) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}