package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.data.repository.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.data.network.impl.ItunesRetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.data.repository.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.impl.TrackListRepositoryImpl
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.interactor.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.impl.TrackListInteractorImpl
import com.example.playlistmaker.settings.data.repository.SettingsRepository
import com.example.playlistmaker.settings.data.repository.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.interactor.impl.SharingInteractorImpl

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

    private fun provideGetSettingsRepository(applicationContext: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            sharedPrefs = provideSharedPreferences(),
            context = applicationContext
        )
    }

    fun provideGetSettingsInteractor(applicationContext: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            themeModeRepository = provideGetSettingsRepository(
                applicationContext
            )
        )
    }

    private fun provideNetworkClient(applicationContext: Context): TrackNetworkClient {
        return ItunesRetrofitNetworkClient(applicationContext)
    }

    private fun provideGetTrackListRepository(applicationContext: Context): TrackListRepository {
        return TrackListRepositoryImpl(provideNetworkClient(applicationContext))
    }

    fun provideGetTrackListInteractor(applicationContext: Context): TrackListInteractor {
        return TrackListInteractorImpl(provideGetTrackListRepository(applicationContext))
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

    private fun provideExternalNavigator(applicationContext: Context): ExternalNavigator{
        return ExternalNavigatorImpl(
            applicationContext = applicationContext
        )
    }

    fun provideShareButton(applicationContext: Context): SharingInteractor{
        return SharingInteractorImpl(provideExternalNavigator(applicationContext))
    }
}









