package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.model.ThemeModeData
import com.example.playlistmaker.domain.model.ThemeMode

object ThemeMapper {
    fun currentTheme(currentTheme: ThemeModeData): ThemeMode {
        return ThemeMode(themeMode = currentTheme.themeMode)
    }

    fun settingTheme(settingTheme: ThemeModeData): ThemeMode {
        return ThemeMode(themeMode = settingTheme.themeMode)
    }

    fun theme(themeMode: ThemeMode): ThemeModeData {
        return ThemeModeData(themeMode = themeMode.themeMode)
    }
}