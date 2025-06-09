package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.data.NewPlayListRepository
import com.example.playlistmaker.medialibrary.data.impl.FavouriteRepositoryImpl
import com.example.playlistmaker.medialibrary.data.impl.NewPlayListRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryRepositoryModule = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }

    single<NewPlayListRepository> {
        NewPlayListRepositoryImpl(androidContext(), get(), get())
    }
}