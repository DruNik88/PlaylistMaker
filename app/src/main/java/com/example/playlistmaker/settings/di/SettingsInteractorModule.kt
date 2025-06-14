package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.interactor.impl.SharingInteractorImpl
import org.koin.dsl.module

val settingsInteractorModel = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            themeModeRepository = get()
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get()
        )
    }
}