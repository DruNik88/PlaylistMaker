package com.example.playlistmaker.search.ui.fragment

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.application.compose.AppTopBar
import com.example.playlistmaker.application.compose.TrackList
import com.example.playlistmaker.application.compose.UpdateClearCreateButton
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.application.compose.theme.Yp_Black
import com.example.playlistmaker.search.ui.state.SearchHistoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchHistoryState,
    onQueryChange: (String) -> Unit,
    onTrackClick: (TrackGeneric) -> Unit,
    onRetry: () -> Unit,
    onClearText: () -> Unit,
    onClearHistory: () -> Unit,
    onGetHistory: () -> Unit
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(isSearchFocused, query.text) {
        if (isSearchFocused && query.text.isEmpty()) {
            onGetHistory()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppTopBar(stringResource(R.string.search))
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.CenterStart

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector_loupe_14_14),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    BasicTextField(
                        value = query,
                        onValueChange = { query = it; onQueryChange(it.text) },
                        singleLine = true,
                        textStyle = AppTextStyles.SearchStyle.copy(color = Yp_Black),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isSearchFocused = focusState.isFocused
                            }
                    ) { innerTextField ->
                        if (query.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search),
                                style = AppTextStyles.SearchStyle,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        innerTextField()
                    }

                    if (query.text.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.vector_close_clear),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(12.dp)
                                .size(16.dp)
                                .clickable {
                                    query = TextFieldValue("")
                                    onClearText()
                                },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is SearchHistoryState.SearchLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchHistoryState.SearchContent -> {
                    TrackList(state.trackList, onTrackClick)
                }

                is SearchHistoryState.HistoryContent -> {
                    if (isSearchFocused) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .padding(top = 24.dp, bottom = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.you_find),
                                    style = AppTextStyles.SearchTextStyle
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            TrackList(state.trackList, onTrackClick)
                            Spacer(modifier = Modifier.height(8.dp))
                            UpdateClearCreateButton(title = stringResource(R.string.clear_history)) { onClearHistory() }
                        }
                    }
                }

                is SearchHistoryState.SearchError -> {
                    ErrorScreen(state.error, onRetry)
                }

                is SearchHistoryState.HistoryEmpty, is SearchHistoryState.HistoryClear -> {
                    if (!isSearchFocused) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                        }
                    }
                }
            }
        }
    }
}