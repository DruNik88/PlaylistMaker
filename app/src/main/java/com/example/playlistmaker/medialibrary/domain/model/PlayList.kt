package com.example.playlistmaker.medialibrary.domain.model

import java.io.Serializable


data class PlayList(
    val id: Long = 0L, //Идентификатор
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageLocalStoragePath: String?, //Путь до локальной картинки изображения
    val count: Int = 0, //Количество треков
    val durationPlayList: Int = 0, //Длительность плейлиста
) : Serializable


