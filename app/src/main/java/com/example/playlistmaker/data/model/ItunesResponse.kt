package com.example.playlistmaker.data.model

class ItunesResponse(
    val resultCount: Int,
    val results: List<TrackApi>
): NetworkResponse()
