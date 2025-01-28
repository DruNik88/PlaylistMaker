package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.model.TrackPlayer
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.state.StatePlayer
import com.example.playlistmaker.player.ui.view_model.AudioPlayerActivityViewModel
import com.example.playlistmaker.search.domain.model.Track

class AudioPlayerActivity : AppCompatActivity() {

//    companion object {
//        private const val RADIUS_IMAGE = 8.0F
//        private const val KEY_TRACK = "track"
//        private const val STATE_DEFAULT = 0
//        private const val STATE_PREPARED = 1
//        private const val STATE_PLAYING = 2
//        private const val STATE_PAUSED = 3
//        private const val DELAY = 1000L
//        private const val AVAILABLE_TIME = 30000L
//    }

    companion object {
        private const val KEY_TRACK = "track"
        private const val RADIUS_IMAGE = 8.0F
    }


    private val viewModel by viewModels<AudioPlayerActivityViewModel> {
        val trackSearch = intent.getParcelableExtra<Track>(KEY_TRACK)
        trackSearch?.let{
            AudioPlayerActivityViewModel.getViewModelFactory(
                trackSearch
            )
        } ?: throw IllegalArgumentException("Track is null")

    }
    private lateinit var binding: ActivityAudioPlayerBinding

    private var mainThreadHandler: Handler? = null

//    private lateinit var trackNameApiAudioPlayer: TextView
//    private lateinit var artistNameApiAudioPlayer: TextView
//    private lateinit var trackTimeApiAudioPlayer: TextView
//    private lateinit var collectionNameApiAudioPlayer: TextView
//    private lateinit var releaseDateApiAudioPlayer: TextView
//    private lateinit var primaryGenreNameApiAudioPlayer: TextView
//    private lateinit var countryApiAudioPlayer: TextView
//    private lateinit var collectionNameGroup: Group
//    private lateinit var artworkApiAudioPlayer: ImageView
//    private lateinit var playbackProgress: TextView
//    private lateinit var playbackControl: ImageView
//    private lateinit var audioPlayerManager: AudioPlayerInteractor
//    private lateinit var trackSearch: Track

//    @SuppressLint("UseCompatLoadingForDrawables")
//    private fun startPlayer() {
//        audioPlayerManager.startPlayer()
//        playbackControl.setImageDrawable(
//            getResources().getDrawable(
//                R.drawable.ic_pause_control,
//                null
//            )
//        )
//        playerState = STATE_PLAYING
//        mainThreadHandler?.post(createUpdateTimerAudioPlayer())
//    }

//    @SuppressLint("UseCompatLoadingForDrawables")
//    private fun pausePlayer() {
//        audioPlayerManager.pausePlayer()
//        playbackControl.setImageDrawable(
//            getResources().getDrawable(
//                R.drawable.ic_playback_control,
//                null
//            )
//        )
//        playerState = STATE_PAUSED
//        mainThreadHandler?.removeCallbacks(createUpdateTimerAudioPlayer())
//    }

//    private fun playbackControl() {
//        when (playerState) {
//            STATE_PLAYING -> {
//                pausePlayer()
//            }
//
//            STATE_PREPARED, STATE_PAUSED -> {
//                startPlayer()
//            }
//        }
//    }

//    private fun createUpdateTimerAudioPlayer(): Runnable {
//        return object : Runnable {
//            @SuppressLint("UseCompatLoadingForDrawables")
//            override fun run() {
//                if (playerState == STATE_PLAYING) {
//                    val reverseTimer =
//                        AVAILABLE_TIME - audioPlayerManager.getCurrentPosition().currentPosition
//
//                    if (reverseTimer >= 0) {
//                        playbackProgress.text =
//                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(reverseTimer)
//                        mainThreadHandler?.postDelayed(this, DELAY)
//                    }
//
//                } else {
//                    mainThreadHandler?.removeCallbacks(this)
//                    playbackControl.setImageDrawable(
//                        getResources().getDrawable(
//                            R.drawable.ic_playback_control,
//                            null
//                        )
//                    )
//                }
//            }
//        }
//    }

    private fun toolBar() {
        setSupportActionBar(binding.toolbarAudioPlayer)

        binding.toolbarAudioPlayer.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            finish()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getShowDataLiveData().observe(this){ showData ->
            renderShowData(showData)
        }

        viewModel.getPlayStatusLiveData().observe(this){ playStatus ->
            renderPlayStatus(playStatus)

        }

//        trackSearch = intent.getParcelableExtra<Track>(KEY_TRACK)


//        viewModel.getScreenStateLiveData().observe(this) { screenState ->
//                // 1
//                when (screenState) {
//                    is TrackScreenState.Content -> {
//                        changeContentVisibility(loading = false)
//                        binding.picture.setImage(screenState.trackModel.pictureUrl)
//                        binding.author.text = screenState.trackModel.author
//                        binding.trackName.text = screenState.trackModel.name
//                    }
//
//                    is TrackScreenState.Loading -> {
//                        changeContentVisibility(loading = true)
//                    }
//                }
//            }
//
//        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
//            changeButtonStyle(playStatus)
//            // 2
//            binding.seekBar.value = playStatus.progress
//        }



        mainThreadHandler = Handler(Looper.getMainLooper())


        toolBar()

//        trackPlayer?.let {
//            audioPlayerManager.preparePlayer(trackPlayer) { state -> playerState = state.state }
//        }

        binding.playbackControl.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun renderPlayStatus(playStatus: StatePlayer) {
        when(playStatus){
            is StatePlayer.Playing -> binding.playbackControl.setImageDrawable(
                getResources().getDrawable(
                    R.drawable.ic_pause_control,
                    null
                ))
            is StatePlayer.Pause,
            is StatePlayer.Prepared-> binding.playbackControl.setImageDrawable(
                getResources().getDrawable(
                    R.drawable.ic_playback_control,
                    null
                )
            )
        }

    }

    private fun renderShowData(showData: ShowData){
        when(showData){
            is ShowData.Content -> showContent(showData.trackModel)
        }
    }

    private fun showContent(trackPlayer: TrackPlayer) {
        val imageUrl = trackPlayer.artworkUrl100
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder_512)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, applicationContext)))
            .into(binding.artworkApiAudioPlayer)
        binding.trackNameApiAudioPlayer.text = trackPlayer.trackName
        binding.artistNameApiAudioPlayer.text = trackPlayer.artistName
        binding.trackTimeApiAudioPlayer.text = trackPlayer.trackTimeMillis
        trackPlayer.collectionName?.let { binding.collectionNameApiAudioPlayer.text = it }
            ?: run { binding.collectionNameGroup.visibility = View.GONE }
        trackPlayer.releaseDate?.let { binding.releaseDateApiAudioPlayer.text = it.substringBefore("-") }
            ?: run { getString(R.string.something_went_wrong) }
        trackPlayer.primaryGenreName?.let { binding.primaryGenreNameApiAudioPlayer.text = it }
            ?: run {
                binding.primaryGenreNameApiAudioPlayer.text = getString(R.string.something_went_wrong)
            }
        trackPlayer.country?.let { binding.countryApiAudioPlayer.text = it }
            ?: run { binding.countryApiAudioPlayer.text = getString(R.string.something_went_wrong) }
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

//    private fun changeContentVisibility(loading: Boolean) {
//        binding.progressBar.isVisible = loading
//
//        binding.playButton.isVisible = !loading
//        binding.picture.isVisible = !loading
//        binding.author.isVisible = !loading
//        binding.trackName.isVisible = !loading
//    }
//
//    private fun changeButtonStyle(playStatus: PlayStatus) {
//        // Меняем вид кнопки проигрывания в зависимости от того, играет сейчас трек или нет
//    }
}
