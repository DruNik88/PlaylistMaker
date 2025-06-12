package com.example.playlistmaker.medialibrary.domain.model


class PlayList(
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageLocalStoragePath: String?, //Путь до локальной картинки изображения
    val count: Int = 0, //Количество треков
)


