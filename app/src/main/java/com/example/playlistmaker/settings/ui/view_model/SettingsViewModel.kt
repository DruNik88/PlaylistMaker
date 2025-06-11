package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.ui.state.SettingsState
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val stateSettingsLiveData = MutableLiveData<SettingsState>()
    fun observeStateSearch(): MutableLiveData<SettingsState> = stateSettingsLiveData

    init {
        getTheme()
    }

    fun shareApp(context: Context) {
        sharingInteractor.shareApp(context)
    }

    fun openTerms(context: Context) {
        sharingInteractor.openTerms(context)
    }

    fun openSupport(context: Context) {
        sharingInteractor.openSupport(context)
    }

//    fun getTheme(): Boolean {
//        return settingsInteractor.getSettingTheme()
//    }

    private fun getTheme() {

        val lightOrDarkTheme = settingsInteractor.getSettingTheme()
        renderThemeState(lightOrDarkTheme)

    }

    fun getSwitchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
        renderThemeState(checked)
    }

    private fun renderThemeState(theme: Boolean) {
        if (!theme) {
            stateSettingsLiveData.postValue(SettingsState.LightMode(light = false))
        } else {
            stateSettingsLiveData.postValue(SettingsState.DarkMode(dark = true))
        }
    }
}








