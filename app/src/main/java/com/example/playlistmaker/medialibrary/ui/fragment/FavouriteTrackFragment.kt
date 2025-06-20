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
import com.example.playlistmaker.application.TrackAdapter
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.databinding.FragmentFavouriteTrackBinding
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.data.mapper.TrackOrListMapper
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTrackFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    val viewModel: FavouriteTrackViewModel by viewModel<FavouriteTrackViewModel>()

    private var _binding: FragmentFavouriteTrackBinding? = null
    private val binding get() = _binding!!

    private var adapter: TrackAdapter<TrackFavourite>? = null
    private lateinit var onTrackClickDebounce: (TrackFavourite) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(
            onItemClickListener = { track -> onTrackClickDebounce(track) },
            showDialog = null
        )

        onTrackClickDebounce = debounce<TrackFavourite>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
                AudioPlayerFragment.createArgs(track)
            )
        }

        viewModel.getFavouriteLiveData().observe(viewLifecycleOwner) { favouriteList ->
            when (favouriteList) {
                is FavouriteData.Content -> {
                    val trackListFavourite = favouriteList.favouriteTrackList
                    showFavouriteList(trackListFavourite)
                }

                is FavouriteData.Empty -> showPlaceHolder()
                else -> {}
            }
        }

        binding.recyclerTrackView.adapter = adapter
    }

    private fun showPlaceHolder() {
        binding.errorImage.isVisible = true
        binding.errorMessage.isVisible = true
        binding.recyclerTrackView.isVisible = false
    }

    private fun showFavouriteList(trackListFavourite: List<TrackFavourite>) {
        adapter?.clearOrUpdateTracks()
        binding.errorImage.isVisible = false
        binding.errorMessage.isVisible = false
        binding.recyclerTrackView.isVisible = true
        adapter?.allUpdateTracks(trackListFavourite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}