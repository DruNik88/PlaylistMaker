package com.example.playlistmaker.player.di

import androidx.room.Room
import com.example.playlistmaker.player.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerDataModule = module {
    single<AppDatabase>{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java,"database.db").build()
    }
}