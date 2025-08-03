package com.example.playlistmaker.player.di

import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel { (track: TrackGeneric) ->
        AudioPlayerViewModel(
            trackSearch = track,
            trackFavouriteInteractor = get(),
            playListPlayerInteractor = get()
        )
    }
}