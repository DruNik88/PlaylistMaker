package com.example.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun getCurrentTheme(): Boolean
    fun getSettingTheme (): Boolean
    fun saveTheme(themeMode: Boolean)
}