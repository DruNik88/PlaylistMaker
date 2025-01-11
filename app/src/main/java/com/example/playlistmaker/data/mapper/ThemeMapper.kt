package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.model.ThemeModeSharedPrefs
import com.example.playlistmaker.domain.model.ThemeMode

object ThemeMapper {
    fun currentTheme(currentTheme: ThemeModeSharedPrefs): ThemeMode {
        return ThemeMode(themeMode = currentTheme.themeMode)
    }

    fun settingTheme(settingTheme: ThemeModeSharedPrefs): ThemeMode {
        return ThemeMode(themeMode = settingTheme.themeMode)
    }

    fun theme(themeMode: ThemeMode): ThemeModeSharedPrefs {
        return ThemeModeSharedPrefs(themeMode = themeMode.themeMode)
    }
}