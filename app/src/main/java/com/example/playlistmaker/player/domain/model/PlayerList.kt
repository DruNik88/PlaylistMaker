package com.example.playlistmaker.player.domain.model

import android.net.Uri

class PlayerList (
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageInnerUri: Uri?, //Путь к файлу изображения
    val count: Int = 0, //Количество треков
)