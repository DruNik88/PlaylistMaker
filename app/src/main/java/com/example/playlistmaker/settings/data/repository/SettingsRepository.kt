package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.domain.model.ThemeMode

interface SettingsRepository {
    fun getCurrentTheme(): ThemeMode
    fun getSettingTheme(): ThemeMode
    fun saveTheme(themeMode: ThemeMode)

}



