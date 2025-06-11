package com.example.playlistmaker.application.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long, //Идентификатор плейлиста
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageInnerUri: String?, //Путь к файлу изображения
//    val listTrackId: Gson? = Gson(), //Список идентификаторов треков
    val countTrack: Int = 0, //Количество треков, добавленных в плейлист
)
