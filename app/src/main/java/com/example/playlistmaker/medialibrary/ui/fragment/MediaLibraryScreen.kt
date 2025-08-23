package com.example.playlistmaker.medialibrary.ui.fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.AppTopBar
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaLibraryScreen(navController: NavController) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppTopBar(stringResource(id = R.string.media_library))
        }
    )

    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
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
                        val viewModel: FavouriteTrackViewModel = koinViewModel()

                        val currentState =
                            viewModel.getFavouriteLiveData().observeAsState(FavouriteData.Empty).value

                        FavouriteTrackScreen(
                            state = currentState,
                            onTrackClick = { track ->
                                navController.navigate(
                                    R.id.action_mediaLibraryFragment_to_audioPlayerFragment2,
                                    AudioPlayerFragment.createArgs(track)
                                )
                            }
                        )
                    }

                    1 -> {
                        val viewModel: PlayListViewModel = koinViewModel()

                        val currentState =
                            viewModel.getPlaylist.observeAsState(PlayListData.Empty).value


                        PlayListScreen(
                            state = currentState,
                            onNewPlaylistClick = {
                                navController.navigate(
                                    R.id.action_mediaLibraryFragment_to_newPlayListFragment
                                )
                            },
                            onPlaylistClick = { playList ->
                                navController.navigate(
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