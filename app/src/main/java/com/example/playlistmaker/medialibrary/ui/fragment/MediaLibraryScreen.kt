package com.example.playlistmaker.medialibrary.ui.fragment

import android.util.Log
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaLibraryScreen(

) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.media_library),
                        style = AppTextStyles.TopAppBarStyle
                    )
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    0 -> FavouriteTrackFragment

                    1 -> PlayListFragment
                }
            }
        }
    }
}