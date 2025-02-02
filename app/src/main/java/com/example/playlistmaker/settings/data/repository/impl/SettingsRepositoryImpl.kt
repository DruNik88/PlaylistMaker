package com.example.playlistmaker.settings.data.repository.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.mapper.ThemeMapper
import com.example.playlistmaker.settings.data.model.ThemeModeData
import com.example.playlistmaker.settings.data.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeMode


class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences, val context: Context) :
    SettingsRepository {

    companion object {
        const val KEY_EDIT_THEME = "key_edit_theme"
    }

    override fun getSettingTheme(): ThemeMode {
        val themeSharedPrefs = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())
        val settingTheme = ThemeMapper.settingTheme(ThemeModeData(themeMode = themeSharedPrefs))
        return settingTheme
    }

    private fun saveTheme(checked: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_EDIT_THEME, checked).apply()
    }

    private fun currentDeviceTheme(): Boolean =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    override fun switchTheme(checked: Boolean) {
        val nightMode = if (checked) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        if (getSettingTheme().themeMode != checked) {
            saveTheme(checked)
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
