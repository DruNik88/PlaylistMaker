package com.example.playlistmaker.medialibrary.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackAdapter
import com.example.playlistmaker.application.converterSecondsToMinutesAndEnding
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.application.durationEndings
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import com.example.playlistmaker.medialibrary.ui.fragment.PlayListViewHolder.Companion.RADIUS_IMAGE
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListInfoViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private var adapter: TrackAdapter<TrackMediaLibraryDomain> = TrackAdapter()
    private lateinit var onTrackClickDebounce: (TrackMediaLibraryDomain) -> Unit

    private lateinit var bottomSheetBehaviorTrackList: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorTrackMenu: BottomSheetBehavior<LinearLayout>

    private var playListActual: PlayList? = null

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

        setupToolBar()

        setupAdapter()

        setupDebounce()

        setupBottomSheet()

        observeViewModel()

        setupOnClickListener()

        binding.playlistTracks.adapter = adapter

    }


    private fun setupToolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarPlaylistInfo)

        binding.toolbarPlaylistInfo.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarPlaylistInfo.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupAdapter() {

        adapter = TrackAdapter(
            onItemClickListener = { track -> onTrackClickDebounce(track) },
            showDialog = { track ->
                val title = getString(R.string.dialog_title_delete_track)
                showDialog(title) { viewModel.deleteTrackFromPlaylist(track) }
            }
        )
    }

    private fun setupDebounce() {

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
    }

    private fun setupBottomSheet() {

        bottomSheetBehaviorTrackList = BottomSheetBehavior.from(binding.bottomSheetTrack).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehaviorTrackMenu = BottomSheetBehavior.from(binding.bottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehaviorTrackMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun observeViewModel() {

        viewModel.getShowLiveData().observe(viewLifecycleOwner) { playListData ->
            when (playListData) {
                is PlayListWithTrackDetail.Content -> {
                    val playList = playListData.playListData.playList
                    val trackList = playListData.playListData.trackList

                    playListActual = playList

                    binding.containerPlaylistInfo.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.share.isEnabled = true
                    parsePlayListData(playList)
                    parsePlayListMenu(playList)
                    adapter.clearOrUpdateTracks()
                    adapter.allUpdateTracks(trackList)
                }

                is PlayListWithTrackDetail.Empty -> {
                    adapter.clearOrUpdateTracks()
                    val playList = playListData.playList

                    playListActual = playList

                    parsePlayListData(playList)
                    parsePlayListMenu(playList)
                    binding.emptyPlaylist.isVisible = true
                }

                is PlayListWithTrackDetail.Loading -> {
                    binding.containerPlaylistInfo.isVisible = false
                    binding.progressBar.isVisible = true
                }
            }

        }
    }

    private fun setupOnClickListener() {

        binding.share.setOnClickListener {

            if (adapter.tracks.isEmpty()) {
                binding.emptyPlaylist.isVisible = true
                binding.emptyPlaylist.setText(R.string.playlist_empty_tracks)
            } else {
                viewModel.sharePlayList()
            }
        }

        binding.menu.setOnClickListener {
            bottomSheetBehaviorTrackMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareBtn.setOnClickListener {
            if (adapter.tracks.isEmpty()) {
                bottomSheetBehaviorTrackMenu.state = BottomSheetBehavior.STATE_HIDDEN
                binding.emptyPlaylist.isVisible = true
                binding.emptyPlaylist.setText(R.string.playlist_empty_tracks)
            } else {
                viewModel.sharePlayList()
            }
        }

        binding.removeBtn.setOnClickListener {
            val title =
                getString(R.string.dialog_title_delete_playlist, playListActual?.title ?: "")
            showDialog(title, true) { viewModel.deletePlayList() }
        }

        binding.editBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_playListInfoFragment_to_newPlayListRedactFragment,
                playListActual?.let { NewPlayListRedactFragment.createArgs(it) }
            )
        }
    }

    private fun showDialog(title: String, delete: Boolean = false, onConfirm: () -> Unit) {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setNegativeButton(R.string.no) { _, _ ->
                onConfirm()
                if (delete) {
                    findNavController().navigateUp()
                }
            }
            .setPositiveButton(R.string.yes) { _, _ ->
            }
            .create()
        confirmDialog.show()
        val ypBlue = ContextCompat.getColor(requireContext(), R.color.yp_blue)
        confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ypBlue)
        confirmDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ypBlue)
    }

    private fun parsePlayListData(playList: PlayList) {

        binding.containerPlaylistInfo.isVisible = true
        binding.progressBar.isVisible = false

        val imageUri =
            playList.imageLocalStoragePath?.let { File(playList.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
            .into(binding.poster)

        binding.playlistTitle.text = playList.title
        binding.playlistDescription.text = playList.description
        binding.playlistTimeTotal.text =
            durationEndings(converterSecondsToMinutesAndEnding(playList.durationPlayList))
        binding.playlistCountTotal.text = trackEndings(playList.count)
    }

    private fun parsePlayListMenu(playLis: PlayList) {

        val imageUri =
            playLis.imageLocalStoragePath?.let { File(playLis.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
            .into(binding.posterBottomSheet)

        binding.posterTitle.text = playLis.title
        binding.trackCount.text = trackEndings(playLis.count)
    }
}
