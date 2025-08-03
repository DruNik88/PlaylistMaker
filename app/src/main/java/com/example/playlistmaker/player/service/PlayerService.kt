package com.example.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PlayerService() : Service() {

    companion object {
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
    }

    private val audioPlayerInteractor: AudioPlayerInteractor by inject()

    private val _playStatus = MutableStateFlow<PlayStatus>(
        PlayStatus(
            progress = 0L,
            isPlaying = false
        )
    )
    val playStatus = _playStatus.asStateFlow()

    private val _data = MutableStateFlow<ShowData>(ShowData.Loading)
    val data = _data.asStateFlow()


    private val binder = PlayerServiceBinder()
    private var trackPlayerDomain: TrackPlayerDomain? = null

    fun getTrack(track: TrackPlayerDomain) {
        trackPlayerDomain = track

        trackPlayerDomain?.let { trackPlayerDomain ->
            audioPlayerInteractor.preparePlayer(
                track = trackPlayerDomain,
                playerObserver = object : AudioPlayerInteractor.AudioPlayerObserver {
                    override fun onProgress(progress: Long) {
                        _playStatus.value = getCurrentPlayStatus().copy(progress = progress)
                    }

                    override fun onComplete() {
                        _playStatus.value =
                            getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                    }

                    override fun onPause() {
                        _playStatus.value = getCurrentPlayStatus().copy(isPlaying = false)
                    }

                    override fun onPlay() {
                        _playStatus.value = getCurrentPlayStatus().copy(isPlaying = true)
                    }

                    override fun onLoad() {
                        _data.value = ShowData.Content(trackModel = trackPlayerDomain)
                    }

                }
            )
        }
    }
// region Service
    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
// endregion

    private fun startForegroundService(){
        createNotificationChannel()

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    private fun stopForegroundService(){
        ServiceCompat.stopForeground(
            this,
            ServiceCompat.STOP_FOREGROUND_REMOVE
        )

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(SERVICE_NOTIFICATION_ID)

        stopSelf()
    }


    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private fun createServiceNotification(): Notification {
        return trackPlayerDomain?.let { trackPlayerDomain ->
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("${trackPlayerDomain.artistName} - ${trackPlayerDomain.trackName}")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()
        } ?: NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Track info unavailable")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            /* id= */ NOTIFICATION_CHANNEL_ID,
            /* name= */ "Music service",
            /* importance= */ NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return _playStatus.value ?: PlayStatus(progress = 0L, isPlaying = false)
    }

    fun playbackControl() {


        CoroutineScope(Dispatchers.Default).launch {
            audioPlayerInteractor.playbackControl()
        }
    }


    fun release() {

        audioPlayerInteractor.release()

    }

    fun appCollapsed(){
        startForegroundService()
    }

    fun appUnfold(){
        stopForegroundService()
    }

    override fun onUnbind(intent: Intent?): Boolean {
              return super.onUnbind(intent)
    }

}