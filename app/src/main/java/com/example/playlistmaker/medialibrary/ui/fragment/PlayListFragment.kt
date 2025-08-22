package com.example.playlistmaker.medialibrary.ui.fragment

import android.R.attr.onClick
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage

import com.example.playlistmaker.application.debounce

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData

import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {
                var currentState by remember {
                    mutableStateOf<PlayListData>(
                        PlayListData.Empty
                    )
                }

                Log.d("History_what", "$currentState")

                viewModel.getPlaylist.observe(viewLifecycleOwner, Observer { state ->
                    currentState = state
                })

                PlayListScreen(
                    state = currentState,
                    onNewPlaylistClick = {
                        findNavController().navigate(
                            R.id.action_mediaLibraryFragment_to_newPlayListFragment)
                    },
                    onPlaylistClick = { playList ->
                        findNavController().navigate(
                            R.id.action_mediaLibraryFragment_to_playListInfoFragment,
                            PlayListInfoFragment.createArgs(playList))
                    },
                )

            }
        }
    }
}
