package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.domain.model.ThemeMode

interface SettingsRepository {
    fun getSettingTheme(): ThemeMode
//    fun saveTheme(checked: Boolean)
    fun switchTheme(checked: Boolean)
}



