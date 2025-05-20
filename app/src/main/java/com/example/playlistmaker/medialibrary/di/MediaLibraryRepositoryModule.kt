package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.data.impl.FavouriteRepositoryImpl
import com.example.playlistmaker.medialibrary.ui.viewmodel.MediaLibraryViewModel
import org.koin.dsl.module

val mediaLibraryRepositoryModule = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }
}