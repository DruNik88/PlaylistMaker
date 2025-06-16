package com.example.playlistmaker.medialibrary.ui.fragment

import android.annotation.SuppressLint
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

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

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

        adapter = TrackAdapter(
            onItemClickListener = { track -> onTrackClickDebounce(track) },
            showDialog = { track -> showDialog(track) }
        )

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

        viewModel.getShowLiveData().observe(viewLifecycleOwner) { playListData ->
            when (playListData) {
                is PlayListWithTrackDetail.Content -> {
                    val playList = playListData.playListData.playList
                    val trackList = playListData.playListData.trackList

                    binding.containerPlaylistInfo.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.share.isEnabled = true
                    parsePlayListData(playList)
                    parsePlayListMenu(playList)
                    adapter?.clearOrUpdateTracks()
                    adapter?.allUpdateTracks(trackList)
                }

                is PlayListWithTrackDetail.Empty -> {
                    val playList = playListData.playList
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
        binding.share.setOnClickListener {

            if(adapter.tracks.isEmpty()) {
                binding.emptyPlaylist.isVisible = true
                binding.emptyPlaylist.setText(R.string.playlist_empty_tracks)
            } else {
                viewModel.sharePlayList()
            }
        }

        binding.menu.setOnClickListener {
            bottomSheetBehaviorTrackMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareBtn.setOnClickListener{
            if(adapter.tracks.isEmpty()) {
                bottomSheetBehaviorTrackMenu.state = BottomSheetBehavior.STATE_HIDDEN
                binding.emptyPlaylist.isVisible = true
                binding.emptyPlaylist.setText(R.string.playlist_empty_tracks)
            } else {
                viewModel.sharePlayList()
            }
        }

        binding.playlistTracks.adapter = adapter

    }

    private fun showDialog(track: TrackMediaLibraryDomain) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title_delete)
            .setNegativeButton(R.string.no) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track)
            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                findNavController().navigateUp()
            }
        val show = confirmDialog.show()
        val ypBlue = ContextCompat.getColor(requireContext(), R.color.yp_blue)
        show.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ypBlue)
        show.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ypBlue)
    }

    private fun parsePlayListData(playLis: PlayList) {

        binding.containerPlaylistInfo.isVisible = true
        binding.progressBar.isVisible = false

        val imageUri =
            playLis.imageLocalStoragePath?.let { File(playLis.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
            .into(binding.poster)

        binding.playlistTitle.text = playLis.title
        binding.playlistDescription.text = playLis.description
        binding.playlistTimeTotal.text =
            playLis.durationPlayList.toString() + " секунд(Не забудь)"
        binding.playlistCountTotal.text = trackEndings(playLis.count)
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
