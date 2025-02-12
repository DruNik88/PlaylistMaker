package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {


    fun shareApp(settingsActivity: SettingsActivity) {
        sharingInteractor.shareApp(settingsActivity)
    }

    fun openTerms(settingsActivity: SettingsActivity) {
        sharingInteractor.openTerms(settingsActivity)
    }

    fun openSupport(settingsActivity: SettingsActivity) {
        sharingInteractor.openSupport(settingsActivity)
    }


    fun getTheme(): Boolean {
        return settingsInteractor.getSettingTheme()
    }

    fun getSwitchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }
}

