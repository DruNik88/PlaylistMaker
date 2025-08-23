package com.example.playlistmaker.search.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {

                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                val viewModel: SearchViewModel = koinViewModel()

                var currentState =
                    viewModel.observeSearchHistory().observeAsState(SearchHistoryState.HistoryEmpty).value

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
                        onClearText = {},
                        onClearHistory = {
                            viewModel.clearHistoryList()
                        },
                        onGetHistory = {
                            viewModel.getHistoryList()
                            viewModel.saveSharedPrefs()
                        }

                    )
                }
            }
        }
    }
}