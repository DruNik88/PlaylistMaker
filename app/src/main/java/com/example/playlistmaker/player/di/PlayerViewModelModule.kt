package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel { (track: TrackSearchDomain) ->
        AudioPlayerViewModel(
            trackSearch = track,
            audioPlayerInteractor = get(),
            trackFavouriteInteractor = get(),
            playListPlayerInteractor = get()
        )
    }
}