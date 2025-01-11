package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.ThemeModeSharedPrefs

interface SharedPrefs {
    fun getCurrentTheme(): ThemeModeSharedPrefs

    fun getSettingTheme(): ThemeModeSharedPrefs

    fun saveTheme(themeMode: ThemeModeSharedPrefs)

}

