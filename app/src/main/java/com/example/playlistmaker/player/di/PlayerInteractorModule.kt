package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.interactor.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.player.domain.interactor.impl.TrackFavouriteInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    factory <AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(
            audioPlayerRepository = get()
        )
    }

    single<TrackFavouriteInteractor> {
        TrackFavouriteInteractorImpl(get())
    }

}