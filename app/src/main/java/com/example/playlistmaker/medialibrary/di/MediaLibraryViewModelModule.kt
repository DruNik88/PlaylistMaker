package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModule = module {
    viewModel{ MediaLibraryViewModel() }
    viewModel{ FavouriteTrackViewModel() }
    viewModel{ PlayListViewModel() }
}


