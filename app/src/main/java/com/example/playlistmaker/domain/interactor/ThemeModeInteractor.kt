package com.example.playlistmaker.domain.interactor

interface ThemeModeInteractor {
    fun getCurrentTheme(): Boolean
    fun getSettingTheme (): Boolean
    fun saveTheme(themeMode: Boolean)
}