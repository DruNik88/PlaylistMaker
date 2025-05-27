package com.example.playlistmaker.medialibrary.domain.model

import com.example.playlistmaker.application.TrackGeneric

data class TrackFavourite (
    override val trackId: Long,
    override val trackName: String?, // Название композиции
    override val artistName: String?, // Имя исполнителя
    override val trackTimeMillis: String?, // Продолжительность трека
    override val artworkUrl100: String?, // Ссылка на изображение обложки
    override val collectionName: String?, //Название альбома
    override val releaseDate: String?, //Год релиза трека
    override val primaryGenreName: String?, //Жанр трека
    override val country: String?, //Страна исполнителя
    override val previewUrl: String?, //Ссылка на отрывок песни
    var isFavourite: Boolean = false, //Нахождение в "Избранном"
): TrackGeneric