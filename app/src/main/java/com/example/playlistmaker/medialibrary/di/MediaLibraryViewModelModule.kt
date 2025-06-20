package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.NewPlayListRedactViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.NewPlayListViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListInfoViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModule = module {
    viewModel { MediaLibraryViewModel() }
    viewModel { FavouriteTrackViewModel(get()) }
    viewModel { PlayListViewModel(get()) }
    viewModel { NewPlayListViewModel(get()) }
    viewModel { PlayListInfoViewModel(get(), get()) }
    viewModel { (playList: PlayList) ->
        NewPlayListRedactViewModel(
            playList = playList,
            newPlayListInteractor = get()
        )
    }
}


