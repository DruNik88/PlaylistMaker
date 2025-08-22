package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.application.compose.TrackList

import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme

import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment

import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTrackFragment()
    }

//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun FavouriteTrackRoot(
//        state: FavouriteData,
//        onTrackClick: (TrackFavourite) -> Unit
//    ) {
//        PlaylistMakerTheme {
//            Scaffold(
//                containerColor = MaterialTheme.colorScheme.surface
//            ) { innerPadding ->
//                FavouriteTrackScreen(
//                    state = state,
//                    onTrackClick = onTrackClick,
//                    modifier = Modifier.padding(innerPadding)
//                )
//            }
//        }
//    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel: FavouriteTrackViewModel by viewModel<FavouriteTrackViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {
                var currentState by remember {
                    mutableStateOf<FavouriteData>(
                        FavouriteData.Empty
                    )
                }

                viewModel.getFavouriteLiveData().observe(viewLifecycleOwner, Observer { state ->
                    currentState = state
                })

                FavouriteTrackScreen(
                    state = currentState,
                    onTrackClick = { track ->
                        findNavController().navigate(
                            R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
                            AudioPlayerFragment.createArgs(track)
                        )
                    }
                )


//                FavouriteTrackRoot(
//                    state = currentState,
//                    onTrackClick = { track ->
//                        findNavController().navigate(
//                R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
//                            AudioPlayerFragment.createArgs(track)
//                        )
//                    }
//                )

            }

        }
    }
}
