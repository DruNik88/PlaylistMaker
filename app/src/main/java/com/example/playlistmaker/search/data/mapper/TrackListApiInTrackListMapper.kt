package com.example.playlistmaker.search.data.mapper


import com.example.playlistmaker.search.data.model.TrackApi
import com.example.playlistmaker.player.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackListApiInTrackListMapper {

    private const val ZERO = 0

    fun map(trackApi: List<TrackApi>): List<Track> {
        val filterTrackListApi: List<TrackApi> = trackApi.filter { track ->
            !track.trackName.isNullOrEmpty() &&
                    !track.artistName.isNullOrEmpty() &&
                    track.trackTimeMillis > ZERO &&
                    !track.artworkUrl100.isNullOrEmpty() &&
                    !track.collectionName.isNullOrEmpty() &&
                    !track.releaseDate.isNullOrEmpty() &&
                    !track.primaryGenreName.isNullOrEmpty() &&
                    !track.country.isNullOrEmpty() &&
                    !track.previewUrl.isNullOrEmpty()
        }
        return filterTrackListApi.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName, // Название композиции
                artistName = it.artistName, // Имя исполнителя
                trackTimeMillis = convertingSeconds(it), // Продолжительность трека
                artworkUrl100 = it.artworkUrl100, // Ссылка на изображение обложки
                collectionName = it.collectionName, //Название альбома
                releaseDate = it.releaseDate, //Год релиза трека
                primaryGenreName = it.primaryGenreName, //Жанр трека
                country = it.country, //Страна исполнителя
                previewUrl = it.previewUrl, //Ссылка на отрывок песни
            )
        }
    }

    private fun convertingSeconds(track: TrackApi): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }
}