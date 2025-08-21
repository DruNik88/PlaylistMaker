package com.example.playlistmaker.medialibrary.ui.fragment

import android.R.attr.onClick
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage

import com.example.playlistmaker.application.debounce

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData

import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
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
                onClick = onNewPlaylistClick
            ) {
                Text(text = stringResource(id = R.string.new_playlist))
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
                text =trackEndings(playlist.count),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(160.dp)
            )
        }
    }


//    val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()
//
//    private var _binding: FragmentPlaylistBinding? = null
//    private val binding get() = _binding!!
//
//    private var adapter: PlayListAdapter? = null
//    private lateinit var onTrackClickDebounce: (PlayList) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {
                var currentState by remember {
                    mutableStateOf<PlayListData>(
                        PlayListData.Empty
                    )
                }

                Log.d("History_what", "$currentState")

                viewModel.getPlaylist.observe(viewLifecycleOwner, Observer { state ->
                    currentState = state
                })

//                fun PlayListScreen(
//                    state: PlayListData,
//                    onNewPlaylistClick: () -> Unit,
//                    onPlaylistClick: (PlayList) -> Unit,
//                    modifier: Modifier = Modifier
//                )

                PlayListScreen(
                    state = currentState,
                    onNewPlaylistClick = {
                        findNavController().navigate(
                            R.id.action_mediaLibraryFragment_to_newPlayListFragment)
                    },
                    onPlaylistClick = { playList ->
                        findNavController().navigate(
                            R.id.action_mediaLibraryFragment_to_playListInfoFragment,
                            PlayListInfoFragment.createArgs(playList))
                    },
                )

            }
        }
    }
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        adapter = PlayListAdapter { playList ->
//            onTrackClickDebounce(playList)
//        }
//
//        onTrackClickDebounce = debounce<PlayList>(
//            delayMillis = CLICK_DEBOUNCE_DELAY,
//            coroutineScope = viewLifecycleOwner.lifecycleScope,
//            useLastParam = false,
//        ) { playList ->
//            findNavController().navigate(
//                R.id.action_mediaLibraryFragment_to_playListInfoFragment,
//                PlayListInfoFragment.createArgs(playList)
//            )
//        }
//
//        viewModel.getPlaylist.observe(viewLifecycleOwner) { playListData ->
//            when (playListData) {
//                is PlayListData.Content -> {
//                    val playList = playListData.playListData
//                    showContent(playList)
//                }
//
//                is PlayListData.Empty -> showPlaceHolder()
//            }
//        }
//
//        binding.recyclerPlayList.adapter = adapter
//
//        binding.buttonNewPlaylist.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_mediaLibraryFragment_to_newPlayListFragment
//            )
//        }
//    }
//
//    private fun showPlaceHolder() {
//        adapter?.clearOrUpdatePlayList()
//        binding.recyclerPlayList.isVisible = false
//        binding.errorImage.isVisible = true
//        binding.errorMessage.isVisible = true
//    }
//
//    private fun showContent(playlist: List<PlayList>) {
//        adapter?.clearOrUpdatePlayList()
//        binding.errorImage.isVisible = false
//        binding.errorMessage.isVisible = false
//        binding.recyclerPlayList.isVisible = true
//        adapter?.allUpdatePlayList(playlist)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//}