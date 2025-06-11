package com.example.playlistmaker.medialibrary.domain.model

import android.net.Uri
import com.google.gson.Gson


class PlayList (
    val title: String?, //Название плейлиста
    val description: String?, //Описание плейлиста
    val imageInnerUri: Uri?, //Путь к файлу изображения
//    val trackId: Gson? = Gson(), //
    val count: Int = 0, //Количество треков
)


