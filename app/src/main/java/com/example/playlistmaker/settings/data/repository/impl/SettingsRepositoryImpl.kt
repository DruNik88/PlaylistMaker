package com.example.playlistmaker.settings.data.repository.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.mapper.ThemeMapper
import com.example.playlistmaker.settings.data.model.ThemeModeData
import com.example.playlistmaker.settings.domain.model.ThemeMode
import com.example.playlistmaker.settings.data.repository.SettingsRepository


class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences, val context: Context) :
    SettingsRepository {

    private var darkTheme = false

    companion object {
        const val KEY_EDIT_THEME = "key_edit_theme"
    }

//    private fun getCurrentTheme(): ThemeMode {
//        val themeSharedPrefs = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())
//        val currentTheme = ThemeMapper.currentTheme(ThemeModeData(themeMode = themeSharedPrefs))
//        return currentTheme
//    }

    override fun getSettingTheme(): ThemeMode {
        val themeSharedPrefs = sharedPrefs.getBoolean(KEY_EDIT_THEME, currentDeviceTheme())

        Log.d("theme_currentDeviceTheme", "тема устройства ${currentDeviceTheme()}")
        Log.d("theme_getTheme", "сохранённая тема $themeSharedPrefs")

        val settingTheme = ThemeMapper.settingTheme(ThemeModeData(themeMode = themeSharedPrefs))

//        switchTheme(settingTheme.themeMode)

        Log.d("theme_settingTheme", "тема для передачи ${settingTheme.themeMode}")

        return settingTheme
    }

    private fun saveTheme(checked: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_EDIT_THEME, checked).apply()
        Log.d("theme_s", "сохраняем тему $checked")
    }

    private fun currentDeviceTheme(): Boolean =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    override fun switchTheme(checked: Boolean) {

//        if (getSettingTheme().themeMode == checked) {
//            Log.d("theme_return", "уже тёмная")
//            return
//        }

//        AppCompatDelegate.setDefaultNightMode(
//        if (checked) {
//            Log.d("theme_night", "night")
//            AppCompatDelegate.MODE_NIGHT_YES
//        } else {
//            Log.d("theme_day", "day")
//            AppCompatDelegate.MODE_NIGHT_NO
//        })
//
//        if (getSettingTheme().themeMode != checked) {
//            saveTheme(checked)
//        }




        val nightMode = if (checked) {
            Log.d("theme_night", "night")
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            Log.d("theme_day", "day")
            AppCompatDelegate.MODE_NIGHT_NO
        }

        if (getSettingTheme().themeMode != checked) {
            saveTheme(checked)
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
