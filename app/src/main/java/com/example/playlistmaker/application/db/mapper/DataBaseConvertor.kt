package com.example.playlistmaker.application.db.mapper

import com.example.playlistmaker.application.db.entity.TrackEntity
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
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

    fun converterTrackEntityToTrackFavourite(trackListEntity: List<TrackEntity>): List<TrackFavourite> {
        return trackListEntity.map { entity ->
            TrackFavourite(
                trackId = entity.trackId,
                trackName = entity.trackName,
                artistName = entity.artistName,
                trackTimeMillis = entity.trackTimeMillis,
                artworkUrl100 = entity.artworkUrl100,
                collectionName = entity.collectionName,
                releaseDate = entity.releaseDate,
                primaryGenreName = entity.primaryGenreName,
                country = entity.country,
                previewUrl = entity.previewUrl,
            )
        }
    }
}