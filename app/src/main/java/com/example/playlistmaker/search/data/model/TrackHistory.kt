package com.example.playlistmaker.search.data.model

data class TrackHistory (
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?, //Год релиза трека
    val primaryGenreName: String?, //Жанр трека
    val country:String?, //Страна исполнителя
    val previewUrl:String?, //Ссылка на отрывок песни
)