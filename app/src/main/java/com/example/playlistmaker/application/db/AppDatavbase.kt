package com.example.playlistmaker.application.db

import androidx.room.Database;
import androidx.room.RoomDatabase
import com.example.playlistmaker.application.db.dao.TrackDao
import com.example.playlistmaker.application.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase(){

    abstract fun getTrackDao(): TrackDao

}
