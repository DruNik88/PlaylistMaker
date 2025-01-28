package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.playlistmaker.application.App
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val shareButton = Creator.provideShareButton()

    fun shareApp(appInstance: Context) {
        shareButton.shareApp(appInstance)
    }

    fun openTerms(appInstance: Context) {
        shareButton.openTerms(appInstance)
    }

    fun openSupport(appInstance: Context) {
        shareButton.openSupport(appInstance)
    }


    fun getTheme(): Boolean {
        return getApplication<App>().currentSwitchTheme()
    }

    fun getSwitchTheme(checked: Boolean) {
        getApplication<App>().switchTheme(checked)
    }

    fun saveTheme() {
        getApplication<App>().saveTheme()
    }
}