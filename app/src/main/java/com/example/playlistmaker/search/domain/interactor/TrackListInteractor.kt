package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.player.domain.model.Track

interface TrackListInteractor {
    fun searchTrackList(expression: String, consumer: TrackListConsumer)

    interface TrackListConsumer{
        fun consume(trackList: Resource<List<Track>>)
    }
}