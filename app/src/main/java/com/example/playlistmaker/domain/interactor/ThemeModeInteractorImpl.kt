package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.ThemeMode
import com.example.playlistmaker.domain.repository.SharedPrefsRepository


class ThemeModeInteractorImpl(
    private val sharedPrefsRepository: SharedPrefsRepository,
) : ThemeModeInteractor {
    override fun getCurrentTheme(): Boolean {
        return sharedPrefsRepository.getCurrentTheme().themeMode
    }

    override fun getSettingTheme(): Boolean {
        return sharedPrefsRepository.getSettingTheme().themeMode
    }

    override fun saveTheme(themeMode: Boolean) {
        sharedPrefsRepository.saveTheme(themeMode = ThemeMode(themeMode = themeMode))
    }
}