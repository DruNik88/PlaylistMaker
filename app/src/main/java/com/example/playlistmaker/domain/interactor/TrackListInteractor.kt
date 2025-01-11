package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track

interface TrackListInteractor {
    fun searchTrackList(expression: String, consumer: TrackListConsumer)

    interface TrackListConsumer{
        fun consume(trackList: Resource<List<Track>>)
    }
}