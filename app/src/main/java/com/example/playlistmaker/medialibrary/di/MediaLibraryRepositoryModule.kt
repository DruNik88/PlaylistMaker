package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.data.NewPlayListRepository
import com.example.playlistmaker.medialibrary.data.PlayListRepository
import com.example.playlistmaker.medialibrary.data.impl.FavouriteRepositoryImpl
import com.example.playlistmaker.medialibrary.data.impl.NewPlayListRepositoryImpl
import com.example.playlistmaker.medialibrary.data.PlayListInfoRepository
import com.example.playlistmaker.medialibrary.data.impl.PlayListInfoRepositoryImpl
import com.example.playlistmaker.medialibrary.data.impl.PlayListRepositoryImpl
import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.data.repository.impl.PlayListPlayerRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryRepositoryModule = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }

    single<NewPlayListRepository> {
        NewPlayListRepositoryImpl(androidContext(), get(), get())
    }

    single<PlayListRepository> {
        PlayListRepositoryImpl(get(), get())
    }

    single<PlayListPlayerRepository> {
        PlayListPlayerRepositoryImpl(get(), get())
    }

    single<PlayListInfoRepository> {
        PlayListInfoRepositoryImpl(get(), get(), androidContext())
    }
}