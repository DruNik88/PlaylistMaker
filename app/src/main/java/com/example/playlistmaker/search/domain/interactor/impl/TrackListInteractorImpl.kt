package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val trackListRepository: TrackListRepository) :
    TrackListInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrackList(
        expression: String,
        consumer: TrackListInteractor.TrackListConsumer
    ) {
        executor.execute {
            consumer.consume(trackListRepository.searchTrackList(expression))
        }
    }
}