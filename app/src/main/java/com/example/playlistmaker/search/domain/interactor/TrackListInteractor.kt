package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.Resource

interface TrackListInteractor {
    fun searchTrackList(expression: String, consumer: TrackListConsumer)

    interface TrackListConsumer{
        fun consume(trackList: Resource<List<com.example.playlistmaker.search.domain.model.TrackSearchDomain>>)
    }
}