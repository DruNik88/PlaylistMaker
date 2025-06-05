package com.example.playlistmaker.application.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.TrackEntity

interface PlayListDao {
    
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertPlayListEntity(playListEntity: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun updatePlayListEntity(playListEntity: PlayListEntity)
}