package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.view_model.AudioPlayerActivityViewModel
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val KEY_TRACK = "track"
        private const val RADIUS_IMAGE = 8.0F
    }


    private val viewModel by viewModels<AudioPlayerActivityViewModel> {
        val trackSearch = intent.getParcelableExtra<TrackSearchDomain>(KEY_TRACK)
        trackSearch?.let {
            AudioPlayerActivityViewModel.getViewModelFactory(
                trackSearch
            )
        } ?: throw IllegalArgumentException("Track is null")

    }
    private lateinit var binding: ActivityAudioPlayerBinding

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

        toolBar()

        viewModel.getShowDataLiveData().observe(this) { showData ->
            when(showData){
                is ShowData.Content -> renderShowData(showData)
                is ShowData.Loading -> loading(loading = true)
            }

        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            renderPlayStatus(playStatus)

        }

        binding.playbackControl.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun renderPlayStatus(playStatus: PlayStatus) {
        if (!playStatus.isPlaying) {
            binding.playbackControl.isEnabled = true
            binding.playbackControl.setImageDrawable(
                getResources().getDrawable(
                    R.drawable.ic_playback_control,
                    null
                )
            )
        } else {
            binding.playbackControl.setImageDrawable(
                getResources().getDrawable(
                    R.drawable.ic_pause_control,
                    null
                )
            )
            binding.playbackProgress.text = playStatus.formatProgress()

        }

    }

    private fun renderShowData(showData: ShowData) {
        when (showData) {
            is ShowData.Content -> {
                loading(loading = false)
                showContent(trackPlayer = showData.trackModel)
            }
            is ShowData.Loading -> loading(loading = true)
        }
    }

    private fun showContent(trackPlayer: TrackPlayerDomain) {
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

        trackPlayer.releaseDate?.let {
            binding.releaseDateApiAudioPlayer.text = it.substringBefore("-")
        }
            ?: run { getString(R.string.something_went_wrong) }

        trackPlayer.primaryGenreName?.let { binding.primaryGenreNameApiAudioPlayer.text = it }
            ?: run {
                binding.primaryGenreNameApiAudioPlayer.text =
                    getString(R.string.something_went_wrong)
            }

        trackPlayer.country?.let { binding.countryApiAudioPlayer.text = it }
            ?: run { binding.countryApiAudioPlayer.text = getString(R.string.something_went_wrong) }
    }

    private fun loading(loading: Boolean){
        binding.layoutPlayer.isVisible = !loading
        binding.layoutProgressBar.isVisible = loading
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

}
