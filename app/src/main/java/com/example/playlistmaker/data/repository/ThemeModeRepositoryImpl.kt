package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.data.mapper.ThemeMapper
import com.example.playlistmaker.data.model.ThemeModeData
import com.example.playlistmaker.domain.model.ThemeMode
import com.example.playlistmaker.domain.repository.ThemeModeRepository


class ThemeModeRepositoryImpl(private val sharedPrefs: SharedPreferences, val context: Context) :
    ThemeModeRepository {

    companion object{
        const val KEY_EDIT_THEME = "key_edit_theme"
    }

    override fun getCurrentTheme(): ThemeMode {
        val themeSharedPrefs = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())
        val currentTheme = ThemeMapper.currentTheme(ThemeModeData(themeMode = themeSharedPrefs))
        return currentTheme
    }

    override fun getSettingTheme(): ThemeMode {
        val themeSharedPrefs = sharedPrefs.getBoolean(KEY_EDIT_THEME, getCurrentTheme().themeMode)
        val settingTheme = ThemeMapper.settingTheme(ThemeModeData(themeMode = themeSharedPrefs))
        return settingTheme
    }

    override fun saveTheme(themeMode: ThemeMode) {
        val themSharedPrefs = ThemeMapper.theme(themeMode)
        sharedPrefs.edit().putBoolean(KEY_EDIT_THEME, themSharedPrefs.themeMode).apply()
    }

    private fun currentDeviceTheme(): Boolean =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}