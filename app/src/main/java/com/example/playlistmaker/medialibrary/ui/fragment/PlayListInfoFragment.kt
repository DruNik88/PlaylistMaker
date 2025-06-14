package com.example.playlistmaker.medialibrary.ui.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackAdapter
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import com.example.playlistmaker.medialibrary.ui.fragment.PlayListViewHolder.Companion.RADIUS_IMAGE
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListInfoViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlayListInfoFragment : Fragment() {
    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L

        private const val KEY_PLAYLIST = "playlist"

        fun createArgs(playlist: PlayList): Bundle =
            bundleOf(KEY_PLAYLIST to playlist)
    }

    val viewModel: PlayListInfoViewModel by viewModel<PlayListInfoViewModel>() {
        val playList = requireArguments().getSerializable(KEY_PLAYLIST) as? PlayList
        playList?.let {
            parametersOf(playList)
        } ?: throw IllegalArgumentException("Playlist is null")
    }

    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

    private var adapter: TrackAdapter<TrackMediaLibraryDomain>? = null
    private lateinit var onTrackClickDebounce: (TrackMediaLibraryDomain) -> Unit

    private fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarPlaylistInfo)

        binding.toolbarPlaylistInfo.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarPlaylistInfo.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar()

        adapter = TrackAdapter { track ->
            onTrackClickDebounce(track)
        }

        onTrackClickDebounce = debounce<TrackMediaLibraryDomain>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
        ) { track ->
            findNavController().navigate(
                R.id.action_playListInfoFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        viewModel.getShowLiveData().observe(viewLifecycleOwner) { playList ->
            when (playList) {
                is PlayListWithTrackDetail.Content -> {
                    val playListWithTrack = playList.playListData
                    showContent(playListWithTrack)
                }

                is PlayListWithTrackDetail.Empty -> {}
                is PlayListWithTrackDetail.Loading -> {
                    binding.containerPlaylistInfo.isVisible = false
                    binding.progressBar.isVisible = true
                }
            }

        }
        binding.playlistTracks.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun showContent(playListWithTrack: PlayListWithTrackMediaLibrary) {

        binding.containerPlaylistInfo.isVisible = true
        binding.progressBar.isVisible = false

        val playList = playListWithTrack.playList
        val trackList = playListWithTrack.trackList

        val imageUri = playList.imageLocalStoragePath?.let { File(playList.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
            .into(binding.poster)

        binding.playlistTitle.text = playList.title
        binding.playlistDescription.text = playList.description
        binding.playlistTimeTotal.text = playList.durationPlayList.toString() + " секунд(Не забудь)"
        binding.playlistCountTotal.text = trackEndings(playList.count)

        adapter?.clearOrUpdateTracks()
        adapter?.allUpdateTracks(trackList)
    }

}
