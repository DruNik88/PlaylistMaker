package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var adapter: PlayListAdapter? = null
    private lateinit var onTrackClickDebounce: (PlayList) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlayListAdapter { playList ->
            onTrackClickDebounce(playList)
        }

        onTrackClickDebounce = debounce<PlayList>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
        ) { playList ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playListInfoFragment,
                PlayListInfoFragment.createArgs(playList)
            )
        }

        viewModel.getPlaylist.observe(viewLifecycleOwner) { playListData ->
            when (playListData) {
                is PlayListData.Content -> {
                    val playList = playListData.playListData
                    showContent(playList)
                }

                is PlayListData.Empty -> showPlaceHolder()
            }
        }

        binding.recyclerPlayList.adapter = adapter

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlayListFragment
            )
        }
    }

    private fun showPlaceHolder() {
        adapter?.clearOrUpdatePlayList()
        binding.recyclerPlayList.isVisible = false
        binding.errorImage.isVisible = true
        binding.errorMessage.isVisible = true
    }

    private fun showContent(playlist: List<PlayList>) {
        adapter?.clearOrUpdatePlayList()
        binding.errorImage.isVisible = false
        binding.errorMessage.isVisible = false
        binding.recyclerPlayList.isVisible = true
        adapter?.allUpdatePlayList(playlist)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}