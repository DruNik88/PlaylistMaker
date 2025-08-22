package com.example.playlistmaker.search.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
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
                            viewModel.addTrackListHistory(track as TrackSearchDomain)
                            findNavController().navigate(
                                R.id.action_searchFragment_to_audioPlayerFragment,
                                AudioPlayerFragment.createArgs(track)
                            )
                        },
                        onRetry = {
                            currentState = SearchHistoryState.SearchLoading
                            viewModel.request(currentState.toString())
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