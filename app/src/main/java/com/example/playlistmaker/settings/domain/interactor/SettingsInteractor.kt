package com.example.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun getSettingTheme(): Boolean
    fun switchTheme(checked: Boolean)
}