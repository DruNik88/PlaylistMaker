package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.ItunesRetrofitNetworkClient
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.HistoryRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeModeRepositoryImpl
import com.example.playlistmaker.data.repository.TrackListRepositoryImpl
import com.example.playlistmaker.data.network.TrackNetworkClient
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.interactor.ThemeModeInteractor
import com.example.playlistmaker.domain.interactor.ThemeModeInteractorImpl
import com.example.playlistmaker.domain.interactor.TrackListInteractor
import com.example.playlistmaker.domain.interactor.TrackListInteractorImpl
import com.example.playlistmaker.domain.interactor.HistoryInteractor
import com.example.playlistmaker.domain.interactor.HistoryInteractorImpl
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.HistoryRepository
import com.example.playlistmaker.domain.repository.ThemeModeRepository
import com.example.playlistmaker.domain.repository.TrackListRepository

private const val SHARED_PREFERENCES_PLAYLIST_MAKER = "shared_preferences_playlist_maker"

object Creator {

    private lateinit var application: Application
    fun initApplication(application: Application) {
        Creator.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(
            SHARED_PREFERENCES_PLAYLIST_MAKER,
            Context.MODE_PRIVATE
        )
    }

    private fun provideGetThemeModeRepository(applicationContext: Context): ThemeModeRepository {
        return ThemeModeRepositoryImpl(
            sharedPrefs = provideSharedPreferences(),
            context = applicationContext
        )
    }

    fun provideGetThemeModeInteractor(applicationContext: Context): ThemeModeInteractor {
        return ThemeModeInteractorImpl(
            themeModeRepository = provideGetThemeModeRepository(
                applicationContext
            )
        )
    }

    private fun provideNetworkClient(): TrackNetworkClient {
        return ItunesRetrofitNetworkClient()
    }

    private fun provideGetTrackListRepository(): TrackListRepository {
        return TrackListRepositoryImpl(provideNetworkClient())
    }

    fun provideGetTrackListInteractor(): TrackListInteractor {
        return TrackListInteractorImpl(provideGetTrackListRepository())
    }

    private fun provideGetHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl(
            sharedPrefs = provideSharedPreferences()
        )
    }

    fun provideGetHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(trackManager = provideGetHistoryRepository())
    }

    private fun provideGetAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideGetAudioPlayerManagerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideGetAudioPlayerRepository())
    }
}









