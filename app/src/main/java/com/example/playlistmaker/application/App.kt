package com.example.playlistmaker.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.application.di.appModule
import com.example.playlistmaker.medialibrary.di.mediaLibraryInteractorModule
import com.example.playlistmaker.medialibrary.di.mediaLibraryRepositoryModule
import com.example.playlistmaker.medialibrary.di.mediaLibraryViewModelModule
import com.example.playlistmaker.player.di.playerInteractorModule
import com.example.playlistmaker.player.di.playerRepositoryModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchInteractorModule
import com.example.playlistmaker.search.di.searchRepositoryModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsInteractorModel
import com.example.playlistmaker.settings.di.settingsRepositoryModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                settingsRepositoryModule,
                settingsInteractorModel,
                settingsViewModelModule,
                searchDataModule,
                searchRepositoryModule,
                searchInteractorModule,
                searchViewModelModule,
                playerRepositoryModule,
                playerInteractorModule,
                playerViewModelModule,
                mediaLibraryViewModelModule,
                mediaLibraryInteractorModule,
                mediaLibraryRepositoryModule
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