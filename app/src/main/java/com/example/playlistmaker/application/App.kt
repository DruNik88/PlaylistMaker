package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.interactor.ThemeModeInteractor

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var getTheme: ThemeModeInteractor


    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        sharedPrefs = Creator.provideSharedPreferences()
        getTheme = Creator.provideGetThemeModeInteractor(applicationContext)

        val themeSetting = getTheme.getSettingTheme()

        switchTheme(themeSetting)
       }

    fun currentSwitchTheme() = getTheme.getCurrentTheme()

    fun saveTheme() = getTheme.saveTheme(themeMode = darkTheme)

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
