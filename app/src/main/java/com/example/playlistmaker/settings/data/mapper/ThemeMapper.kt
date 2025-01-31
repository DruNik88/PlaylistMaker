package com.example.playlistmaker.settings.data.mapper

import com.example.playlistmaker.settings.data.model.ThemeModeData
import com.example.playlistmaker.settings.domain.model.ThemeMode

object ThemeMapper {

    fun settingTheme(settingTheme: ThemeModeData): ThemeMode {
        return ThemeMode(themeMode = settingTheme.themeMode)
    }

}