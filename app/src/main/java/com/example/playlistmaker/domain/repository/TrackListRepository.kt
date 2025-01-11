package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track

interface TrackListRepository {
    fun searchTrackList (expression: String): Resource<List<Track>>
}

