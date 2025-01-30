package com.example.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun getSettingTheme(): Boolean
//    fun saveTheme()
    fun switchTheme(checked: Boolean)
}