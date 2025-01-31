package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.domain.model.ThemeMode

interface SettingsRepository {
    fun getSettingTheme(): ThemeMode
    fun switchTheme(checked: Boolean)
}



