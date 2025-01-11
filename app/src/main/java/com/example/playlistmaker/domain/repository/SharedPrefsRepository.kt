package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.ThemeMode

interface SharedPrefsRepository {
    fun getCurrentTheme(): ThemeMode
    fun getSettingTheme(): ThemeMode
    fun saveTheme(themeMode: ThemeMode)

}



