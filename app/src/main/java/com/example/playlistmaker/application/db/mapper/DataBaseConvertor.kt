package com.example.playlistmaker.application.db.mapper

import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.PlayListTrackCrossRef
import com.example.playlistmaker.application.db.entity.PlayListWithTracks
import com.example.playlistmaker.application.db.entity.TrackEntity
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

class DataBaseConvertor {

    fun playListTrackCrossRefDomainToPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefDomain): PlayListTrackCrossRef {
        return PlayListTrackCrossRef(
            playlistId = playListTrackCrossRefDomain.playlistId,
            trackId = playListTrackCrossRefDomain.trackId
        )
    }

    fun converterPlayListWithTrackEntityToPlayListWithTrack(playListEntity: List<PlayListWithTracks>): List<PlayListWithTrack> {
        return playListEntity.map { entity ->
            PlayListWithTrack(
                playList = PlayerList(
                    id = entity.playlist.id,
                    title = entity.playlist.title,
                    description = entity.playlist.description,
                    imageLocalStoragePath = entity.playlist.imageLocalStoragePath,
                    count = entity.playlist.countTrack
                ),
                trackList = entity.tracks.map { trackListEntity ->
                    converterTrackEntityToTrackDomain(trackListEntity)
                }
            )
        }

    }

    private fun converterTrackEntityToTrackDomain(trackEntity: TrackEntity): TrackPlayerDomain {
        return TrackPlayerDomain(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl,
            isFavourite = trackEntity.isFavourite,
        )
    }

    fun converterTrackDomainToTrackEntity(trackPlayerDomain: TrackPlayerDomain): TrackEntity {
        return TrackEntity(
            trackId = trackPlayerDomain.trackId,
            trackName = trackPlayerDomain.trackName,
            artistName = trackPlayerDomain.artistName,
            trackTimeMillis = trackPlayerDomain.trackTimeMillis,
            artworkUrl100 = trackPlayerDomain.artworkUrl100,
            collectionName = trackPlayerDomain.collectionName,
            releaseDate = trackPlayerDomain.releaseDate,
            primaryGenreName = trackPlayerDomain.primaryGenreName,
            country = trackPlayerDomain.country,
            previewUrl = trackPlayerDomain.previewUrl,
            isFavourite = trackPlayerDomain.isFavourite,
        )
    }

    fun converterTrackEntityToTrackFavourite(trackListEntity: List<TrackEntity>): List<TrackFavourite> {
        return trackListEntity.map { entity ->
            TrackFavourite(
                trackId = entity.trackId,
                trackName = entity.trackName,
                artistName = entity.artistName,
                trackTimeMillis = entity.trackTimeMillis,
                artworkUrl100 = entity.artworkUrl100,
                collectionName = entity.collectionName,
                releaseDate = entity.releaseDate,
                primaryGenreName = entity.primaryGenreName,
                country = entity.country,
                previewUrl = entity.previewUrl,
                isFavourite = entity.isFavourite,
            )
        }
    }

    fun converterPlayListDomainToPlayListEntity(playListDomain: PlayList): PlayListEntity {
        return PlayListEntity(
            id = 0L,
            title = playListDomain.title,
            description = playListDomain.description,
            imageLocalStoragePath = playListDomain.imageLocalStoragePath,
            countTrack = playListDomain.count
        )
    }

    fun converterPlayListEntityToPlayListDomain(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { entity ->
            PlayList(
                title = entity.title,
                description = entity.description,
                imageLocalStoragePath = entity.imageLocalStoragePath,
                count = entity.countTrack
            )
        }
    }

    fun converterPlayerListDomainToPlayListEntity(playerList: PlayerList): PlayListEntity {
        return PlayListEntity(
            id = playerList.id,
            title = playerList.title,
            description = playerList.description,
            imageLocalStoragePath = playerList.imageLocalStoragePath,
            countTrack = playerList.count
        )
    }
}