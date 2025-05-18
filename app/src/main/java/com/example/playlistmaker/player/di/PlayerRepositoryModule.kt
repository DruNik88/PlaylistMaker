package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.data.db.mapper.DataBaseConvertor
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.data.repository.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.data.repository.impl.TrackFavouriteRepositoryImpl
import org.koin.dsl.module

val playerRepositoryModule = module{

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }

    factory { DataBaseConvertor() }

    single<TrackFavouriteRepository> {
        TrackFavouriteRepositoryImpl(get(), get())
    }

}