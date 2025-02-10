package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.application.di.appModule
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.di.settingsInteractorModel
import com.example.playlistmaker.settings.di.settingsRepositoryModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var getTheme: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                settingsRepositoryModule, settingsInteractorModel, settingsViewModelModule,
            )
        }

        val getTheme: SettingsInteractor = get<SettingsInteractor>()
        val theme = getTheme.getSettingTheme()
        when (theme) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}