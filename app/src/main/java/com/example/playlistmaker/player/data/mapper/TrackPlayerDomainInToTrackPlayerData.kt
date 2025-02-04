package com.example.playlistmaker.player.data.mapper

import com.example.playlistmaker.player.data.model.TrackPlayerData
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

object TrackPlayerDomainInToTrackPlayerData {
    fun trackPlayerDomainInToTrackPlayerData(track: TrackPlayerDomain): TrackPlayerData {
        return TrackPlayerData(
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

    private fun getCoverArtwork(track: TrackPlayerDomain) =
        track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}

