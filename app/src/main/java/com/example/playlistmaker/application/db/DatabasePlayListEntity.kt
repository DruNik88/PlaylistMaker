package com.example.playlistmaker.application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.application.db.dao.PlayListDao
import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.PlayListTrackCrossRef
import com.example.playlistmaker.application.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [PlayListEntity::class, TrackEntity::class, PlayListTrackCrossRef::class]
)
abstract class DatabasePlayListEntity : RoomDatabase() {

    abstract fun getPlayListDao(): PlayListDao

}
