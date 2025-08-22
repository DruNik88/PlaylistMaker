package com.example.playlistmaker.medialibrary.ui.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.application.compose.TrackList
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData

@Composable
fun FavouriteTrackScreen(
    state: FavouriteData,
    onTrackClick: (TrackGeneric) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
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
}