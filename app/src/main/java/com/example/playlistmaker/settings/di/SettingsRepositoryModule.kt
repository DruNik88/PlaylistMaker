package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.repository.SettingsRepository
import com.example.playlistmaker.settings.data.repository.impl.SettingsRepositoryImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsRepositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            sharedPrefs = get(),
            context = get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            applicationContext = androidContext()
        )
    }
}
