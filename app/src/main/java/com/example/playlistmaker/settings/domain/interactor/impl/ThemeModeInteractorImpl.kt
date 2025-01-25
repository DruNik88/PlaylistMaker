package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.domain.interactor.ThemeModeInteractor
import com.example.playlistmaker.domain.model.ThemeMode
import com.example.playlistmaker.settings.data.repository.SettingsRepository


class ThemeModeInteractorImpl(
    private val themeModeRepository: SettingsRepository,
) : ThemeModeInteractor {
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