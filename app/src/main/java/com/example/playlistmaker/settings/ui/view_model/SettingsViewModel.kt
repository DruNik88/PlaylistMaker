package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.ui.fragment.SettingsFragment
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {


    fun shareApp(context: Context) {
        sharingInteractor.shareApp(context)
    }

    fun openTerms(context: Context) {
        sharingInteractor.openTerms(context)
    }

    fun openSupport(context: Context) {
        sharingInteractor.openSupport(context)
    }


    fun getTheme(): Boolean {
        return settingsInteractor.getSettingTheme()
    }

    fun getSwitchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }
}

