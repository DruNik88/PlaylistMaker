package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeMode
import com.example.playlistmaker.settings.data.repository.SettingsRepository


class SettingsInteractorImpl(
    private val themeModeRepository: SettingsRepository,
) : SettingsInteractor {
    override fun getCurrentTheme(): Boolean {
        return themeModeRepository.getCurrentTheme().themeMode
    }

    override fun getSettingTheme(): Boolean {
        return themeModeRepository.getSettingTheme().themeMode
    }

    override fun saveTheme(themeMode: Boolean) {
        themeModeRepository.saveTheme(themeMode = ThemeMode(themeMode = themeMode))
    }
}