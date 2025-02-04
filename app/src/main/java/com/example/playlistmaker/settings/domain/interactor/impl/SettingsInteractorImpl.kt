package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.data.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor

class SettingsInteractorImpl(
    private val themeModeRepository: SettingsRepository,
) : SettingsInteractor {

    override fun getSettingTheme(): Boolean {
        return themeModeRepository.getSettingTheme().themeMode
    }

    override fun switchTheme(checked: Boolean) {
        themeModeRepository.switchTheme(checked)
    }

}