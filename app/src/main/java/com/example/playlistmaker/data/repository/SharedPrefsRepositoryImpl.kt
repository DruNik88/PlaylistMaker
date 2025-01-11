package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.mapper.ThemeMapper
import com.example.playlistmaker.domain.model.ThemeMode
import com.example.playlistmaker.domain.repository.SharedPrefsRepository

class SharedPrefsRepositoryImpl(private val sharedPrefsImpl: SharedPrefs): SharedPrefsRepository {
    override fun getCurrentTheme(): ThemeMode {
        val currentTheme = sharedPrefsImpl.getCurrentTheme()
        return ThemeMapper.currentTheme(currentTheme)
    }

    override fun getSettingTheme(): ThemeMode {
        val settingTheme = sharedPrefsImpl.getCurrentTheme()
        return ThemeMapper.settingTheme(settingTheme)
    }

    override fun saveTheme(themeMode: ThemeMode){
        val them = ThemeMapper.theme(themeMode)
        sharedPrefsImpl.saveTheme(them)
    }
}