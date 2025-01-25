package com.example.playlistmaker.player.data.mapper

import com.example.playlistmaker.settings.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.player.data.model.StateAudioPlayerData
import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer

object StateAudioPlayerMapper {
    fun stateAudioPlayer(stateAudioPlayer: StateAudioPlayerData): StateAudioPlayer {
        return StateAudioPlayer(state = stateAudioPlayer.state)
    }
    fun currentPositionAudioPlayer(currentPositionAudioPlayerData: CurrentPositionAudioPlayerData): CurrentPositionAudioPlayer {
        val currentPositionAudioPlayer = CurrentPositionAudioPlayer(currentPosition = currentPositionAudioPlayerData.currentPosition)
        return currentPositionAudioPlayer
    }

}