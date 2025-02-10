package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.data.repository.impl.AudioPlayerRepositoryImpl
import org.koin.dsl.module

val playerRepositoryModule = module{

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }

}