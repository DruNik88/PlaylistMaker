package com.example.playlistmaker.application.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {
    
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertPlayListEntity(playListEntity: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun updatePlayListEntity(playListEntity: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlayListEntity(): Flow<List<PlayListEntity>>
}