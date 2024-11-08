package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object{
        const val SHARED_PREFERENCES_PLAYLIST_MAKER = "shared_preferences_playlist_maker"
        const val KEY_EDIT_THEME = "key_edit_theme"
    }

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_PLAYLIST_MAKER, MODE_PRIVATE)
        val themeSetting = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentSwitchTheme())
        switchTheme(themeSetting)
       }

    private fun currentDeviceTheme(): Boolean = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun currentSwitchTheme() = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())

    fun saveTheme() {
        sharedPrefs.edit()
            .putBoolean(KEY_EDIT_THEME, darkTheme)
            .apply()
    }

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
