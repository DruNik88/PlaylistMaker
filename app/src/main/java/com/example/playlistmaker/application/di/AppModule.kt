package com.example.playlistmaker.application.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.DatabaseTrackEntity
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val SHARED_PREFERENCES_PLAYLIST_MAKER = "shared_preferences_playlist_maker"

val appModule = module {

    single<DatabaseTrackEntity>{
        Room.databaseBuilder(androidContext(), DatabaseTrackEntity::class.java,"database_track.db").build()
    }

    single<DatabasePlayListEntity> {
        Room.databaseBuilder(androidContext(), DatabasePlayListEntity::class.java, "database_playlist.db").build()
    }

    single {
        androidContext()
            .getSharedPreferences(SHARED_PREFERENCES_PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }

    factory { Gson() }
}