package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val RADIUS_IMAGE = 8.0F
        private const val KEY_TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
        private const val AVAILABLE_TIME = 30000L
    }

    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null

    private lateinit var trackNameApiAudioPlayer: TextView
    private lateinit var artistNameApiAudioPlayer: TextView
    private lateinit var trackTimeApiAudioPlayer: TextView
    private lateinit var collectionNameApiAudioPlayer: TextView
    private lateinit var releaseDateApiAudioPlayer: TextView
    private lateinit var primaryGenreNameApiAudioPlayer: TextView
    private lateinit var countryApiAudioPlayer: TextView
    private lateinit var collectionNameGroup: Group
    private lateinit var artworkApiAudioPlayer: ImageView
    private lateinit var playbackProgress: TextView
    private lateinit var playbackControl: ImageView
    private lateinit var audioPlayerManager: AudioPlayerInteractor

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun startPlayer() {
        audioPlayerManager.startPlayer()
        playbackControl.setImageDrawable(
            getResources().getDrawable(
                R.drawable.ic_pause_control,
                null
            )
        )
        playerState = STATE_PLAYING
        mainThreadHandler?.post(createUpdateTimerAudioPlayer())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun pausePlayer() {
        audioPlayerManager.pausePlayer()
        playbackControl.setImageDrawable(
            getResources().getDrawable(
                R.drawable.ic_playback_control,
                null
            )
        )
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(createUpdateTimerAudioPlayer())
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun createUpdateTimerAudioPlayer(): Runnable {
        return object : Runnable {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val reverseTimer =
                        AVAILABLE_TIME - audioPlayerManager.getCurrentPosition().currentPosition

                    if (reverseTimer >= 0) {
                        playbackProgress.text =
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(reverseTimer)
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }

                } else {
                    mainThreadHandler?.removeCallbacks(this)
                    playbackControl.setImageDrawable(
                        getResources().getDrawable(
                            R.drawable.ic_playback_control,
                            null
                        )
                    )
                }
            }
        }
    }

    private fun toolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun dataAudioPlayerActivity(track: Track) {
        val imageUrl = getCoverArtwork(track)
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder_512)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, applicationContext)))
            .into(artworkApiAudioPlayer)
        trackNameApiAudioPlayer.text = track.trackName
        artistNameApiAudioPlayer.text = track.artistName
        trackTimeApiAudioPlayer.text = track.trackTimeMillis
        track.collectionName?.let { collectionNameApiAudioPlayer.text = it }
            ?: run { collectionNameGroup.visibility = View.GONE }
        track.releaseDate?.let { releaseDateApiAudioPlayer.text = it.substringBefore("-") }
            ?: run { getString(R.string.something_went_wrong) }
        track.primaryGenreName?.let { primaryGenreNameApiAudioPlayer.text = it }
            ?: run {
                primaryGenreNameApiAudioPlayer.text = getString(R.string.something_went_wrong)
            }
        track.country?.let { countryApiAudioPlayer.text = it }
            ?: run { countryApiAudioPlayer.text = getString(R.string.something_went_wrong) }
    }

    private fun getCoverArtwork(track: Track) =
        track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(KEY_TRACK)

        trackNameApiAudioPlayer = findViewById(R.id.track_name_api_audio_player)
        collectionNameGroup = findViewById(R.id.collection_name_group)
        artistNameApiAudioPlayer = findViewById(R.id.artist_name_api_audio_player)
        trackTimeApiAudioPlayer = findViewById(R.id.track_time_api_audio_player)
        releaseDateApiAudioPlayer = findViewById(R.id.release_date_api_audio_player)
        primaryGenreNameApiAudioPlayer = findViewById(R.id.primary_genre_name_api_audio_player)
        countryApiAudioPlayer = findViewById(R.id.country_api_audio_player)
        artworkApiAudioPlayer = findViewById(R.id.artwork_api_audio_player)
        collectionNameApiAudioPlayer = findViewById(R.id.collection_name_api_audio_player)
        playbackProgress = findViewById(R.id.playback_progress)
        playbackControl = findViewById(R.id.playback_control)

        audioPlayerManager = Creator.provideGetAudioPlayerManagerInteractor()

        toolBar()

        track?.let { dataAudioPlayerActivity(track) }

        track?.let {
            audioPlayerManager.preparePlayer(track) { state -> playerState = state.state }
        }

        playbackControl.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING) {
            pausePlayer()
        }
        mainThreadHandler?.removeCallbacks(createUpdateTimerAudioPlayer())
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(createUpdateTimerAudioPlayer())
        audioPlayerManager.release()
    }
}
