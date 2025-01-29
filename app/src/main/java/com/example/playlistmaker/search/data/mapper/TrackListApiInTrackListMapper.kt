package com.example.playlistmaker.search.data.mapper


import com.example.playlistmaker.search.data.model.TrackSearchData
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import java.text.SimpleDateFormat
import java.util.Locale

object TrackListApiInTrackListMapper {

    private const val ZERO = 0

    fun map(trackApi: List<TrackSearchData>): List<TrackSearchDomain> {
        val filterTrackListApi: List<TrackSearchData> = trackApi.filter { track ->
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
            TrackSearchDomain(
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

    private fun convertingSeconds(track: TrackSearchData): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }
}