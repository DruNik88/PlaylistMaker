package com.example.playlistmaker.application.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

@Composable
fun TrackList(tracks: List<TrackGeneric>, onTrackClick: (TrackGeneric) -> Unit) {
    LazyColumn {
        items(tracks) { track ->
            TrackItem(track, onTrackClick)
        }
    }
}

@Composable
fun TrackItem(track: TrackGeneric, onTrackClick: (TrackGeneric) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onTrackClick(track) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier
                .width(45.dp)
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

        Image(
            painter = painterResource(id = R.drawable.vector_arrow_forward),
            contentDescription = null,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            alignment = Alignment.Center,
        )
    }
}