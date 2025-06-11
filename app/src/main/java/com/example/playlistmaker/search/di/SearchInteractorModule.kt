package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.interactor.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.impl.TrackListInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single<TrackListInteractor> {
        TrackListInteractorImpl(
            trackListRepository = get()
        )
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(
            trackManager = get()
        )
    }
}
