package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.data.model.ThemeModeSharedPrefs


class SharedPrefsImpl(private val sharedPrefs: SharedPreferences, val context: Context) :
    SharedPrefs {

    companion object{
        const val KEY_EDIT_THEME = "key_edit_theme"
    }

    override fun getCurrentTheme(): ThemeModeSharedPrefs {
        val theme = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())
        return ThemeModeSharedPrefs(themeMode = theme)
    }

    override fun getSettingTheme(): ThemeModeSharedPrefs {
        val theme = sharedPrefs.getBoolean(KEY_EDIT_THEME, getCurrentTheme().themeMode)
        return ThemeModeSharedPrefs(themeMode = theme)
    }

    override fun saveTheme(themeMode: ThemeModeSharedPrefs) {
        sharedPrefs.edit().putBoolean(KEY_EDIT_THEME, themeMode.themeMode).apply()
    }

    private fun currentDeviceTheme(): Boolean =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}