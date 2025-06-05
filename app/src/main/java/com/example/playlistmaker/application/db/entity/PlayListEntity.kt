package com.example.playlistmaker.application.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long, //Идентификатор плейлиста
    val title: String, //Название плейлиста
    val description: String, //Описание плейлиста
    val imageInnerUri: String, //Путь к файлу изображения
    val listTrackId: String, //Список идентификаторов треков
    val countTrack: Int, //Количество треков, добавленных в плейлист
)
