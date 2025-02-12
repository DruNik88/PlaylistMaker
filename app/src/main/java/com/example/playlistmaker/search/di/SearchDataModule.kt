package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.data.network.impl.ItunesRetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL = "https://itunes.apple.com"

val searchDataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single<TrackNetworkClient> {
        ItunesRetrofitNetworkClient(get(), androidContext())
    }
}