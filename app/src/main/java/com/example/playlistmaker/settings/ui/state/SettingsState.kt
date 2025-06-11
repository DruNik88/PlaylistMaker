package com.example.playlistmaker.settings.ui.state

sealed class SettingsState {
    data class LightMode(
        val light: Boolean
    ) : SettingsState()

    data class DarkMode(
        val dark: Boolean
    ) : SettingsState()
}