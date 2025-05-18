package com.example.playlistmaker.player.data.db.mapper

import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

class DataBaseConvertor {
    fun converterTrackDomainToTrackEntity(trackPlayerDomain: TrackPlayerDomain): TrackEntity {
        return TrackEntity(
            trackId = trackPlayerDomain.trackId,
            trackName = trackPlayerDomain.trackName,
            artistName = trackPlayerDomain.artistName,
            trackTimeMillis = trackPlayerDomain.trackTimeMillis,
            artworkUrl100 = trackPlayerDomain.artworkUrl100,
            collectionName = trackPlayerDomain.collectionName,
            releaseDate = trackPlayerDomain.releaseDate,
            primaryGenreName = trackPlayerDomain.primaryGenreName,
            country = trackPlayerDomain.country,
            previewUrl = trackPlayerDomain.previewUrl,
        )
    }
}