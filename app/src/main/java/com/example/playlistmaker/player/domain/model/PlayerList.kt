package com.example.playlistmaker.player.domain.model

class PlayerList(
    val id: Long, //Идентификатор
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageLocalStoragePath: String?, //Путь до локальной картинки изображения
    val count: Int = 0, //Количество треков
    val durationPlayList: Int = 0, //Длительность плейлиста
)
