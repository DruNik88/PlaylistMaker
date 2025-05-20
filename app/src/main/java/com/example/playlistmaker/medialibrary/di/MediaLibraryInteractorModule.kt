package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.domain.interactor.FavouriteInteractor
import com.example.playlistmaker.medialibrary.domain.interactor.impl.FavouriteInteractorImpl
import org.koin.dsl.module

val mediaLibraryInteractorModule = module {

    single<FavouriteInteractor> {
        FavouriteInteractorImpl(get())
    }

}