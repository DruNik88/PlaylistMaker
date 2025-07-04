package com.example.playlistmaker.medialibrary.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.NewPlayListRepository
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class NewPlayListRepositoryImpl(
    val context: Context,
    private val converter: DataBaseConvertor,
    private val dataBase: DatabasePlayListEntity
) : NewPlayListRepository {

    private lateinit var imageName: String

    override suspend fun saveImageUri(uri: Uri): String {
        imageName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, imageName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.absolutePath
    }

    override suspend fun saveDataBase(newPlayList: PlayList) {
        val converter = converter.converterPlayListDomainToPlayListEntity(newPlayList)
        withContext(Dispatchers.IO) { dataBase.getPlayListDao().insertPlayListEntity(converter) }
    }

    override suspend fun updateDataBase(playList: PlayList) {
        val playListEntity = converter.converterPlayListDomainToPlayListEntity(playList)
        withContext(Dispatchers.IO) {
            dataBase.getPlayListDao().updatePlayListEntity(playListEntity)
        }
    }

}