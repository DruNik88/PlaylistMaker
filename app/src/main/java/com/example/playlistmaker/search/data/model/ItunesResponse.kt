package com.example.playlistmaker.search.data.model

class ItunesResponse(
    val resultCount: Int,
    val results: List<TrackSearchData>
): NetworkResponse()
