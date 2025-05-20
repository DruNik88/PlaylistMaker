package com.example.playlistmaker.application.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.application.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertTrackEntity(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    fun getTrackListEntity(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    fun getTrackListIdEntity(): List<Long>
}