package com.example.playlistmaker.application.db.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM playlist_table WHERE id = :playListId")
    fun getPlayListWithTrackDetailEntity(playListId: Long): Flow<PlayListWithTracks>


    @Delete
    fun deleteTrackFromPlaylist(crossRef: PlayListTrackCrossRef)

    @Query("SELECT * FROM playlist_track_cross_ref WHERE trackId = :trackId")
    fun getCrossRefsByTrackId(trackId: Long): List<PlayListTrackCrossRef>

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    fun deleteTrack(trackId: Long)

    @Query(
        """UPDATE playlist_table
         SET 
            countTrack = countTrack -1,
            durationPlayList = durationPlayList - :durationTrack 
        WHERE id = :playlistId
        """
    )
    fun updateCountTrackAndDurationPlayList(playlistId: Long, durationTrack: Int)

    @Delete
    fun deletePlayListEntity(playListEntity: PlayListEntity)

    @Query("DELETE FROM track_table WHERE trackId NOT IN (SELECT DISTINCT trackId FROM playlist_track_cross_ref)")
    fun deleteUnusedTracks()

    @Transaction
    fun deletePlayList(playListEntity: PlayListEntity) {
        deletePlayListEntity(playListEntity)
        deleteUnusedTracks()
    }


}
