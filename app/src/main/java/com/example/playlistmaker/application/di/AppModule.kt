package com.example.playlistmaker.application.di

import android.content.Context
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.data.network.impl.ItunesRetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL = "https://itunes.apple.com"
private const val SHARED_PREFERENCES_PLAYLIST_MAKER = "shared_preferences_playlist_maker"

val appModule = module {

    single {
        androidContext()
            .getSharedPreferences(SHARED_PREFERENCES_PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }

    factory { Gson() }
}