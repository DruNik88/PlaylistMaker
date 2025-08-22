package com.example.playlistmaker.search.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme
import com.example.playlistmaker.application.compose.theme.YP_Text_Gray
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment

import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel: SearchViewModel by viewModel<SearchViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {

                var currentState by remember {
                    mutableStateOf<SearchHistoryState>(
                        SearchHistoryState.HistoryEmpty
                    )
                }

                Log.d("History_what", "$currentState")

                viewModel.observeSearchHistory().observe(viewLifecycleOwner, Observer { state ->
                    currentState = state
                })
                PlaylistMakerTheme {
                    SearchScreen(
                        state = currentState,
                        onQueryChange = { query ->
                            viewModel.searchRequestText(query)
                        },
                        onTrackClick = { track ->
                            viewModel.addTrackListHistory(track)
                            findNavController().navigate(
                                R.id.action_searchFragment_to_audioPlayerFragment,
                                AudioPlayerFragment.createArgs(track)
                            )
                        },
                        onRetry = {
                            currentState = SearchHistoryState.SearchLoading
                            viewModel.request(currentState.toString()) // или конкретный текст запроса
                        },
                        onClearText = {
                            // Если нужно, можно очистить историю поиска
                        },
                        onClearHistory = {
                            viewModel.clearHistoryList()
                        },
                        onGetHistory = {
                            // Запрашиваем историю при создании экрана
                            viewModel.getHistoryList()
                            viewModel.saveSharedPrefs()
                        }

                    )
                }
            }
        }
    }
}