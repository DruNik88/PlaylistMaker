package com.example.playlistmaker.medialibrary.ui.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.UpdateClearCreateButton
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData

@Composable
fun PlayListScreen(
    state: PlayListData,
    onNewPlaylistClick: () -> Unit,
    onPlaylistClick: (PlayList) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UpdateClearCreateButton(stringResource(R.string.new_playlist)) { onNewPlaylistClick() }

        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            is PlayListData.Empty -> {
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                        .padding(start = 16.dp, end = 16.dp),
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
