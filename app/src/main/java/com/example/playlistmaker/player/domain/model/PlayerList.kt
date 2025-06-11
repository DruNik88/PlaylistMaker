package com.example.playlistmaker.player.domain.model

import android.net.Uri
import com.google.gson.Gson

class PlayerList (
    val id: Long, //Идентификатор
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageInnerUri: Uri?, //Путь к файлу изображения
//    val trackId: Gson? = Gson(), //Список id треков
    val count: Int = 0, //Количество треков
)