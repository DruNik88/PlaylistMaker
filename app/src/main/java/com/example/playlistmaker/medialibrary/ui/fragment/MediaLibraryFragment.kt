package com.example.playlistmaker.medialibrary.ui.fragment

import android.R.attr.maxLines
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

import com.example.playlistmaker.medialibrary.ui.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.application.theme.AppTextStyles
import com.example.playlistmaker.application.theme.PlaylistMakerTheme
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaLibraryFragment : Fragment() {

    private val viewModel: MediaLibraryViewModel by viewModel<MediaLibraryViewModel>()

//    private var _binding: FragmentMediaLibraryBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var tabMediator: TabLayoutMediator

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MediaLibraryScreen(

    ) {

        // CoroutineScope нам нужен для анимации при переключении табов
        val scope = rememberCoroutineScope()

        // Ключевое состояние Pager, при инициализации которого указывается количество экранов, которые можно переключать
        val pagerState = rememberPagerState(pageCount = { 2 })

        // Индекс текущего выбранного таба
        val selectedTabIndex = remember { pagerState.currentPage }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.media_library),
//                            style = AppTextStyles.TopAppBarStyle.copy(
//                                color = MaterialTheme.colorScheme.onBackground
//                            )
                            style = AppTextStyles.TopAppBarStyle
                        )
                    })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                .padding(top = it.calculateTopPadding())
                    .padding(innerPadding)

            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            height = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        // отключаем изменение прозрачности
                        selectedContentColor = Color.Unspecified,
                        unselectedContentColor = Color.Unspecified,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.favourite_track),
                                style = AppTextStyles.TabRowAndButtonStyle,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                    )

                    Tab(
                        selected = pagerState.currentPage == 1,
                        // отключаем изменение прозрачности
                        selectedContentColor = Color.Unspecified,
                        unselectedContentColor = Color.Unspecified,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.playlist),
                                style = AppTextStyles.TabRowAndButtonStyle,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> {
                            val viewModel: FavouriteTrackViewModel by viewModel<FavouriteTrackViewModel>()

                            var currentState by remember {
                                mutableStateOf<FavouriteData>(
                                    FavouriteData.Empty
                                )
                            }

                            Log.d("History_what", "$currentState")

                            viewModel.getFavouriteLiveData()
                                .observe(viewLifecycleOwner, Observer { state ->
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

                        1 -> {
                            val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()
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
                                        R.id.action_mediaLibraryFragment_to_newPlayListFragment
                                    )
                                },
                                onPlaylistClick = { playList ->
                                    findNavController().navigate(
                                        R.id.action_mediaLibraryFragment_to_playListInfoFragment,
                                        PlayListInfoFragment.createArgs(playList)
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }

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


    @Composable
    fun PlayListScreen(
        state: PlayListData,
        onNewPlaylistClick: () -> Unit,
        onPlaylistClick: (PlayList) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Кнопка "Создать плейлист"
            Button(

                shape = RoundedCornerShape(54.dp),
                onClick = onNewPlaylistClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.new_playlist),
                    style = AppTextStyles.TabRowAndButtonStyle,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (state) {
                is PlayListData.Empty -> {
                    // Состояние пустого плейлиста
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nothing_found),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 46.dp)
                                .size(
                                    width = dimensionResource(id = R.dimen.activity_search_image_view_error_width),
                                    height = dimensionResource(id = R.dimen.activity_search_image_view_error_height)
                                )
                        )

                        Text(
                            text = stringResource(id = R.string.empty_playlist),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                is PlayListData.Content -> {
                    // Список плейлистов (Grid 2 колонки)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(state.playListData) { playlist ->
                            PlaylistItem(
                                playlist = playlist,
                                onClick = { onPlaylistClick(playlist) }
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun PlaylistItem(
        playlist: PlayList,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {}
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
                .width(160.dp) // фиксируем ширину, как в XML
                .clickable { onClick() }
        ) {
            // Карточка с картинкой
            Card(
                modifier = Modifier
                    .size(160.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                AsyncImage(
                    model = playlist.imageLocalStoragePath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Название плейлиста
            playlist.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(160.dp)
                )
            }

            // Кол-во треков
            Text(
                text = trackEndings(playlist.count),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(160.dp)
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme { MediaLibraryScreen() }

            }

        }
    }
}
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewPager.adapter = MediaLibraryPagerAdapter(
//            fragmentManager = childFragmentManager,
//            lifecycle = lifecycle
//        )
//        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = getString(R.string.favourite_track)
//                1 -> tab.text = getString(R.string.playlist)
//            }
//        }
//        tabMediator.attach()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//        tabMediator.detach()
//    }
//}
