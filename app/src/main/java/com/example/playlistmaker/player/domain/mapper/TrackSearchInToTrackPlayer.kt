package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.player.domain.model.TrackPlayer
import com.example.playlistmaker.search.data.model.TrackHistory
import com.example.playlistmaker.search.domain.model.Track

object TrackSearchInToTrackPlayer {
    fun trackSearchInToTrackPlayer(track: Track): TrackPlayer {
        return TrackPlayer(
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

    private fun getCoverArtwork(track: Track) =
        track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}

