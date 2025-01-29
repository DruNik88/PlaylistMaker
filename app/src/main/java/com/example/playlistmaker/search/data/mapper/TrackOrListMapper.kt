package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackHistory
import com.example.playlistmaker.search.data.model.TrackListHistory
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackList

object TrackOrListMapper {

    fun trackDomaInToTrackData(track: TrackSearchDomain): TrackHistory {
        return TrackHistory(
            trackId = track.trackId,
            trackName = track.trackName, // Название композиции
            artistName = track.artistName, // Имя исполнителя
            trackTimeMillis = track.trackTimeMillis, // Продолжительность трека
            artworkUrl100 = track.artworkUrl100, // Ссылка на изображение обложки
            collectionName = track.collectionName, //Название альбома
            releaseDate = track.releaseDate, //Год релиза трека
            primaryGenreName = track.primaryGenreName, //Жанр трека
            country = track.country, //Страна исполнителя
            previewUrl = track.previewUrl, //Ссылка на отрывок песни
        )
    }

    fun listDataToListDomain(list: TrackListHistory): TrackList {
        return TrackList(list = list.list.map {
            TrackSearchDomain(
                trackId = it.trackId,
                trackName = it.trackName, // Название композиции
                artistName = it.artistName, // Имя исполнителя
                trackTimeMillis = it.trackTimeMillis, // Продолжительность трека
                artworkUrl100 = it.artworkUrl100, // Ссылка на изображение обложки
                collectionName = it.collectionName, //Название альбома
                releaseDate = it.releaseDate, //Год релиза трека
                primaryGenreName = it.primaryGenreName, //Жанр трека
                country = it.country, //Страна исполнителя
                previewUrl = it.previewUrl, //Ссылка на отрывок песни
            )
        }.toMutableList())
    }
}