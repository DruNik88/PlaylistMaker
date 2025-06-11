package com.example.playlistmaker.application.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlayListWithTracks(
    @Embedded val playlist: PlayListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trackId",
        associateBy = Junction(
            value = PlayListTrackCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "trackId")
    )
    val tracks: List<TrackEntity>
)