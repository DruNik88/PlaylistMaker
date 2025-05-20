package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackHistory
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

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
            isFavourite = track.isFavourite, //Нахождение в "Избранном"
        )
    }

    fun trackDataInToTrackDomain(track: TrackHistory): TrackSearchDomain {
        return TrackSearchDomain(
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
            isFavourite = track.isFavourite, //Нахождение в "Избранном"
        )
    }



    fun listDataToListDomain(list: MutableList<TrackHistory>): List<TrackSearchDomain> {
        return list.map {
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
                isFavourite = it.isFavourite, //Нахождение в "Избранном"
            )
        }.toList()
    }
}