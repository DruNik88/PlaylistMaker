package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.ItunesRetrofitNetworkClient
import com.example.playlistmaker.data.repository.AudioPlayerManagerImpl
import com.example.playlistmaker.data.repository.AudioPlayerRepository
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SharedPrefs
import com.example.playlistmaker.data.repository.SharedPrefsImpl
import com.example.playlistmaker.data.repository.SharedPrefsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackListRepositoryImpl
import com.example.playlistmaker.data.repository.TrackManagerImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.data.repository.TrackRepository
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.interactor.AudioPlayerManagerInteractor
import com.example.playlistmaker.domain.interactor.AudioPlayerManagerInteractorImpl
import com.example.playlistmaker.domain.interactor.ThemeModeInteractor
import com.example.playlistmaker.domain.interactor.ThemeModeInteractorImpl
import com.example.playlistmaker.domain.interactor.TrackListInteractor
import com.example.playlistmaker.domain.interactor.TrackListInteractorImpl
import com.example.playlistmaker.domain.interactor.TrackManagerInteractor
import com.example.playlistmaker.domain.interactor.TrackManagerInteractorImpl
import com.example.playlistmaker.domain.repository.AudioPlayerManager
import com.example.playlistmaker.domain.repository.SharedPrefsRepository
import com.example.playlistmaker.domain.repository.TrackListRepository
import com.example.playlistmaker.domain.repository.TrackManager

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

    private fun provideGetSharedPrefs(applicationContext: Context): SharedPrefs {
        return SharedPrefsImpl(
            sharedPrefs = provideSharedPreferences(),
            context = applicationContext
        )
    }

    private fun provideGetSharedPrefsRepository(applicationContext: Context): SharedPrefsRepository {
        return SharedPrefsRepositoryImpl(sharedPrefsImpl = provideGetSharedPrefs(applicationContext))
    }

    fun provideGetThemeModeInteractor(applicationContext: Context): ThemeModeInteractor {
        return ThemeModeInteractorImpl(
            sharedPrefsRepository = provideGetSharedPrefsRepository(
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

    private fun provideGetTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            sharedPrefs = provideSharedPreferences(),
        )
    }

    private fun provideGetTrackManager(): TrackManager {
        return TrackManagerImpl(trackRepository = provideGetTrackRepository())
    }

    fun provideGetTrackManagerInteractor(): TrackManagerInteractor {
        return TrackManagerInteractorImpl(trackManager = provideGetTrackManager())
    }

    private fun provideGetAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    private fun provideGetAudioPlayerManager(): AudioPlayerManager {
        return AudioPlayerManagerImpl(provideGetAudioPlayerRepository())
    }

    fun provideGetAudioPlayerManagerInteractor(): AudioPlayerManagerInteractor {
        return AudioPlayerManagerInteractorImpl(provideGetAudioPlayerManager())
    }
}









