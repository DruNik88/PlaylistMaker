package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

object TrackGenericInToTrackPlayerDomain {
    fun trackGenericToTrackPlayerDomain(track: TrackGeneric): TrackPlayerDomain {
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
            isFavourite = track.isFavourite, //Нахождение в "Избранном"
        )
    }

    private fun getCoverArtwork(track: TrackGeneric) =
        track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}
