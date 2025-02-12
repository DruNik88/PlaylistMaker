package com.example.playlistmaker.application.di

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val SHARED_PREFERENCES_PLAYLIST_MAKER = "shared_preferences_playlist_maker"

val appModule = module {

    single {
        androidContext()
            .getSharedPreferences(SHARED_PREFERENCES_PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }

    factory { Gson() }
}