package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

object TrackSearchDomainInToTrackPlayerDomain {
    fun trackSearchDomainInToTrackPlayerDomain(track: TrackSearchDomain): TrackPlayerDomain {
        return TrackPlayerDomain(
            trackId = track.trackId,
            trackName = track.trackName, // Название композиции
            artistName = track.artistName, // Имя исполнителя
            trackTimeMillis = track.trackTimeMillis, // Продолжительность трека
            artworkUrl100 = getCoverArtwork(track), // Ссылка на изображение обложки
            collectionName = track.collectionName, //Название альбома
            releaseDate = track.releaseDate, //Год релиза трека
            primaryGenreName = track.primaryGenreName, //Жанр трека
            country = track.country, //Страна исполнителя
            previewUrl = track.previewUrl, //Ссылка на отрывок песни
        )
    }

    private fun getCoverArtwork(track: TrackSearchDomain) =
        track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}


