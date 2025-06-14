package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.domain.interactor.FavouriteInteractor
import com.example.playlistmaker.medialibrary.domain.interactor.NewPlayListInteractor
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInteractor
import com.example.playlistmaker.medialibrary.domain.interactor.impl.FavouriteInteractorImpl
import com.example.playlistmaker.medialibrary.domain.interactor.impl.NewPlayListInteractorImpl
import com.example.playlistmaker.medialibrary.domain.interactor.impl.PlayListInteractorImpl
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.impl.PlayListPlayerInteractorImpl
import org.koin.dsl.module

val mediaLibraryInteractorModule = module {

    single<FavouriteInteractor> {
        FavouriteInteractorImpl(get())
    }

    single<NewPlayListInteractor> {
        NewPlayListInteractorImpl(get())
    }

    single<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }

    single<PlayListPlayerInteractor> {
        PlayListPlayerInteractorImpl(get())
    }

}