package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.data.model.StateAudioPlayerData
import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer

object StateAudioPlayerMapper {
    fun stateAudioPlayer(stateAudioPlayer: StateAudioPlayerData): StateAudioPlayer {
        return StateAudioPlayer(state = stateAudioPlayer.state)
    }
    fun currentPositionAudioPlayer(currentPositionAudioPlayerData: CurrentPositionAudioPlayerData): CurrentPositionAudioPlayer {
        val currentPositionAudioPlayer = CurrentPositionAudioPlayer(currentPosition = currentPositionAudioPlayerData.currentPosition)
        return currentPositionAudioPlayer
    }

}