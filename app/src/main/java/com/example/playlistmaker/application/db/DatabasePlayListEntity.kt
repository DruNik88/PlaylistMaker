package com.example.playlistmaker.application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.application.db.dao.PlayListDao
import com.example.playlistmaker.application.db.entity.PlayListEntity

@Database(version = 1, entities = [PlayListEntity::class])
abstract class DatabasePlayListEntity: RoomDatabase(){

    abstract fun getPlayListDao(): PlayListDao

}
