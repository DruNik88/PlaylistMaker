package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.repository.TrackListRepository
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