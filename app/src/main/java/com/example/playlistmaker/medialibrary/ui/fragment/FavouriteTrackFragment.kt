package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.playlistmaker.R

import com.example.playlistmaker.application.TrackAdapter
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.application.theme.AppTextStyles
import com.example.playlistmaker.application.theme.PlaylistMakerTheme
import com.example.playlistmaker.databinding.FragmentFavouriteTrackBinding

import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState

import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTrackFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
//
//    val viewModel: FavouriteTrackViewModel by viewModel<FavouriteTrackViewModel>()
//
//    private var _binding: FragmentFavouriteTrackBinding? = null
//    private val binding get() = _binding!!
//
//    private var adapter: TrackAdapter<TrackFavourite>? = null
//    private lateinit var onTrackClickDebounce: (TrackFavourite) -> Unit

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FavouriteTrackRoot(
        state: FavouriteData,
        onBackClick: () -> Unit,
        onTrackClick: (TrackFavourite) -> Unit
    ) {
        PlaylistMakerTheme {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surface
//                topBar = {
//                    TopAppBar(
//                        title = {
//                            Text(
//                                text = stringResource(id = R.string.favourites_title),
//                                style = MaterialTheme.typography.titleLarge
//                            )
//                        },
//                        navigationIcon = {
//                            IconButton(onClick = { onBackClick() }) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_arrow_back),
//                                    contentDescription = stringResource(id = R.string.back)
//                                )
//                            }
//                        },
//                        colors = TopAppBarDefaults.topAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.surface,
//                            titleContentColor = MaterialTheme.colorScheme.onSurface,
//                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
//                        )
//                    )
//                }
            ) { innerPadding ->
                FavouriteTrackScreen(
                    state = state,
                    onTrackClick = onTrackClick,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    @Composable
    fun FavouriteTrackScreen(
        state: FavouriteData,
        onTrackClick: (TrackFavourite) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (state) {
                is FavouriteData.Content -> {
                    TrackList(
                        tracks = state.favouriteTrackList,
                        onTrackClick = onTrackClick
                    )
                }

                is FavouriteData.Empty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 106.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nothing_found),
                            contentDescription = null,
                            modifier = Modifier.size(
                                width = dimensionResource(id = R.dimen.activity_search_image_view_error_width),
                                height = dimensionResource(id = R.dimen.activity_search_image_view_error_height)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.media_library_empty),
                            style = AppTextStyles.YouMediaLibraryIsEmptyStyle

                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TrackList(tracks: List<TrackFavourite>, onTrackClick: (TrackFavourite) -> Unit) {
        LazyColumn {
            items(tracks) { track ->
                TrackItem(track, onTrackClick)
            }
        }
    }

    @Composable
    fun TrackItem(track: TrackFavourite, onTrackClick: (TrackFavourite) -> Unit) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onTrackClick(track) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Арт
            Card(
                modifier = Modifier
                    .width(45.dp)  // замените на dimension, если хотите брать из @dimen
                    .height(45.dp),
                shape = RoundedCornerShape(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                AsyncImage(
                    model = track.artworkUrl100,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Текстовая часть
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp), // чтобы текст не прилипал к стрелке
                verticalArrangement = Arrangement.Center
            ) {
                track.trackName?.let {
                    Text(
                        text = it,
                        style = AppTextStyles.TrackItemTrackNameStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    track.artistName?.let {
                        Text(
                            text = it,
                            style = AppTextStyles.TrackItemArtistNameAndTrackTime,
//                                modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .padding(5.dp),
                        painter = painterResource(id = R.drawable.ic_ellipse),
                        contentDescription = null
                    )

                    track.trackTimeMillis?.let {
                        Text(
                            text = it,
                            style = AppTextStyles.TrackItemArtistNameAndTrackTime
                        )
                    }
                }
            }

            // Стрелка
            Image(
                painter = painterResource(id = R.drawable.vector_arrow_forward),
                contentDescription = null,
                modifier = Modifier
                    .width(24.dp)  // замените на @dimen/track_view_image_view_arrow_forward_width
                    .height(24.dp),
                alignment = Alignment.Center,
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentFavouriteTrackBinding.inflate(inflater, container, false)
//        return binding.root
        val viewModel: FavouriteTrackViewModel by viewModel<FavouriteTrackViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {
                var currentState by remember {
                    mutableStateOf<FavouriteData>(
                        FavouriteData.Empty
                    )
                }

                Log.d("History_what", "$currentState")

                viewModel.getFavouriteLiveData().observe(viewLifecycleOwner, Observer { state ->
                    currentState = state
                })

                FavouriteTrackRoot(
                    state = currentState,
                    onBackClick = { requireActivity().onBackPressed() },
                    onTrackClick = { track ->
                        findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
                            AudioPlayerFragment.createArgs(track)
                        )
                    }
                )

            }

        }
    }
}


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        adapter = TrackAdapter(
//            onItemClickListener = { track -> onTrackClickDebounce(track) },
//            showDialog = null
//        )
//
//        onTrackClickDebounce = debounce<TrackFavourite>(
//            delayMillis = CLICK_DEBOUNCE_DELAY,
//            coroutineScope = viewLifecycleOwner.lifecycleScope,
//            useLastParam = false,
//        ) { track ->
//            findNavController().navigate(
//                R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
//                AudioPlayerFragment.createArgs(track)
//            )
//        }
//
//        viewModel.getFavouriteLiveData().observe(viewLifecycleOwner) { favouriteList ->
//            when (favouriteList) {
//                is FavouriteData.Content -> {
//                    val trackListFavourite = favouriteList.favouriteTrackList
//                    showFavouriteList(trackListFavourite)
//                }
//
//                is FavouriteData.Empty -> showPlaceHolder()
//                else -> {}
//            }
//        }
//
//        binding.recyclerTrackView.adapter = adapter
//    }
//
//    private fun showPlaceHolder() {
//        binding.errorImage.isVisible = true
//        binding.errorMessage.isVisible = true
//        binding.recyclerTrackView.isVisible = false
//    }
//
//    private fun showFavouriteList(trackListFavourite: List<TrackFavourite>) {
//        adapter?.clearOrUpdateTracks()
//        binding.errorImage.isVisible = false
//        binding.errorMessage.isVisible = false
//        binding.recyclerTrackView.isVisible = true
//        adapter?.allUpdateTracks(trackListFavourite)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//}