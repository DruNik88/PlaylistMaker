package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.data.repository.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.impl.TrackListRepositoryImpl
import org.koin.dsl.module

val searchRepositoryModule = module{

    single<TrackListRepository> {
        TrackListRepositoryImpl(
            trackNetworkClient = get(),
            database = get())
    }

    factory <HistoryRepository> {
        HistoryRepositoryImpl(
            gson = get(),
            sharedPrefs = get(),
            database = get())
    }
}
