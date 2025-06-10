package com.example.playlistmaker.application.db.mapper

import android.net.Uri
import com.example.playlistmaker.application.db.entity.PlayListEntity
import com.example.playlistmaker.application.db.entity.TrackEntity
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

class DataBaseConvertor {
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
    fun converterPlayListDomainToPlayListEntity(playListDomain: PlayList): PlayListEntity{
      return  PlayListEntity(
          id = 0L,
          title = playListDomain.title,
          description = playListDomain.description,
          imageInnerUri = uriToString(playListDomain.imageInnerUri),
          listTrackId = "",
          countTrack = 0
      )
    }

    fun converterPlayListEntityToPlayListDomain(playList: List<PlayListEntity>): List<PlayList>{
        return playList.map { entity ->
            PlayList(
                title = entity.title,
                description = "",
                imageInnerUri = entity.imageInnerUri?.let { stringToUri(it) },
                count = entity.countTrack
            )
        }
    }

    fun converterPlayListEntityToPlayerListDomain(playList: List<PlayListEntity>): List<PlayerList>{
        return playList.map { entity ->
            PlayerList(
                title = entity.title,
                description = "",
                imageInnerUri = entity.imageInnerUri?.let { stringToUri(it) },
                count = entity.countTrack
            )
        }
    }

    private fun uriToString(uri: Uri?): String{
        return uri.toString()
    }
    private fun stringToUri(string: String): Uri{
        return string.let {Uri.parse(it)}
    }
}