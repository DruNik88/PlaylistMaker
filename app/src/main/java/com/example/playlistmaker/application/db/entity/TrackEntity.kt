package com.example.playlistmaker.application.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?, //Год релиза трека
    val primaryGenreName: String?, //Жанр трека
    val country: String?, //Страна исполнителя
    val previewUrl: String?, //Ссылка на отрывок песни
    var isFavourite: Boolean = false, //Нахождение в "Избранном"
    val timeAdded: Long = System.currentTimeMillis(), //Время добавления трека
)