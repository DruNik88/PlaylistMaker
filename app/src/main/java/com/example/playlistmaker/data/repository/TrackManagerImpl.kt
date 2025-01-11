package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.mapper.TrackOrListMapper
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList
import com.example.playlistmaker.domain.repository.TrackManager

class TrackManagerImpl(private val trackRepository: TrackRepository) : TrackManager {
    override fun addTrackListHistory(track: Track) {
        val trackData = TrackOrListMapper.trackDomainToTrackData(track)
        trackRepository.addTrackListHistory(trackData)
    }

    override fun getListHistory(): TrackList {
        val listData = trackRepository.getListHistory()
        val listDomain = TrackOrListMapper.listDataToListDomain(listData)
        return listDomain
    }

    override fun clearHistory() {
        trackRepository.clearHistory()
    }

    override fun saveSharedPrefs() {
        trackRepository.saveSharedPrefs()
    }
}