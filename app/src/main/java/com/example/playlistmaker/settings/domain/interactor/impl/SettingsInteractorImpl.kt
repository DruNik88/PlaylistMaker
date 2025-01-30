package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.data.repository.SettingsRepository


class SettingsInteractorImpl(
    private val themeModeRepository: SettingsRepository,
) : SettingsInteractor {

    override fun getSettingTheme(): Boolean {
        return themeModeRepository.getSettingTheme().themeMode
    }

//    override fun saveTheme() {
//        themeModeRepository.saveTheme(checked)
//    }

    override fun switchTheme(checked: Boolean) {
        themeModeRepository.switchTheme(checked)
    }

}