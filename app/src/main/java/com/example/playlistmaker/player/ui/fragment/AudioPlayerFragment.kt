package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    companion object {
        private const val KEY_TRACK = "track"
        private const val RADIUS_IMAGE = 8.0F

       fun createArgs(track: TrackSearchDomain): Bundle =
           bundleOf(KEY_TRACK to track)
    }

    private val viewModel: AudioPlayerViewModel by viewModel{
        val trackSearch = requireArguments().getSerializable(KEY_TRACK) as? TrackSearchDomain
        trackSearch?.let {
            parametersOf(trackSearch)
        } ?: throw IllegalArgumentException("Track is null")
    }

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!


    private fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAudioPlayer)

        binding.toolbarAudioPlayer.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar()

        viewModel.getShowDataLiveData().observe(viewLifecycleOwner) { showData ->
            when(showData){
                is ShowData.Content -> renderShowData(showData)
                is ShowData.Loading -> loading(loading = true)
            }
        }

        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { playStatus ->
            renderPlayStatus(playStatus)
        }

        binding.playbackControl.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addFavourite.setOnClickListener{
            viewModel.addFavourite()
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
                resources.getDrawable(
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
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder_512)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
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
        favourite(trackPlayer)
    }

    private fun favourite(trackPlayer: TrackPlayerDomain){
        if (trackPlayer.isFavourite) {
            binding.addFavourite.setImageResource(R.drawable.ic_added_favourite)
        } else {
            binding.addFavourite.setImageResource(R.drawable.ic_add_favourite)
        }
    }

    private fun loading(loading: Boolean){
        binding.layoutPlayer.isVisible = !loading
        binding.layoutProgressBar.isVisible = loading
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.release()
    }

}
