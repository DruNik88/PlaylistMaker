package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.ThemeMode
import com.example.playlistmaker.domain.repository.ThemeModeRepository


class ThemeModeInteractorImpl(
    private val themeModeRepository: ThemeModeRepository,
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