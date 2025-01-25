package com.example.playlistmaker.settings.domain.interactor

interface ThemeModeInteractor {
    fun getCurrentTheme(): Boolean
    fun getSettingTheme (): Boolean
    fun saveTheme(themeMode: Boolean)
}