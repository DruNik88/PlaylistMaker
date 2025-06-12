package com.example.playlistmaker.application.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.PlayListTrackCrossRef
import com.example.playlistmaker.application.db.entity.PlayListWithTracks
import com.example.playlistmaker.application.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertPlayListEntity(playListEntity: PlayListEntity)

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrackEntity(trackEntity: TrackEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlayListEntity(playListEntity: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlayListEntity(): Flow<List<PlayListEntity>>

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlayListWithTrackEntity(): Flow<List<PlayListWithTracks>>

    @Transaction
    @Insert(entity = PlayListTrackCrossRef::class, onConflict = OnConflictStrategy.ABORT)
    fun addPlayListTrackCrossRef(playListTrack: PlayListTrackCrossRef)
}
