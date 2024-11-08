package com.example.playlistmaker.api

import com.example.playlistmaker.Track

class ItunesResponse(
    val resultCount: Int,
    val results: MutableList<Track>
)
