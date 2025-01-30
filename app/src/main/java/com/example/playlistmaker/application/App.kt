package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
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
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Log.d("theme_app_night", "установлена ночь $theme")
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Log.d("theme_app_day", "установлен день $theme")
        }
    }
}