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


//    private var internetAccessibility: InternetAccessibility? = null
//
//    private var _binding: FragmentSearchBinding? = null
//    private val binding get() = _binding!!
//
//    companion object {
//        const val DEFAULT_VALUE = ""
//        private const val CLICK_DEBOUNCE_DELAY = 1000L
//    }
//
//    private var adapter: TrackAdapter<TrackSearchDomain>? = null
//    private lateinit var onTrackClickDebounce: (TrackSearchDomain) -> Unit
//    private var requestText: String = ""

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchScreen(
        state: SearchHistoryState,
        onQueryChange: (String) -> Unit,
        onTrackClick: (TrackSearchDomain) -> Unit,
        onRetry: () -> Unit,
        onClearText: () -> Unit,
        onClearHistory: () -> Unit,
        onGetHistory: () -> Unit
    ) {
        var query by remember { mutableStateOf(TextFieldValue("")) }
        var isSearchFocused by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.search),
                            style = AppTextStyles.TopAppBarStyle
                        )
                    }
                )
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
                            shape = RoundedCornerShape(8.dp) // аналог твоего drawable
                        ),
                    contentAlignment = Alignment.CenterStart

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
//                            .padding(horizontal = 8.dp)
                        ,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Лупа
                        Icon(
                            painter = painterResource(id = R.drawable.vector_loupe_14_14),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(12.dp)
                                .size(14.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        // Поле ввода
                        BasicTextField(
                            value = query,
                            onValueChange = { query = it; onQueryChange(it.text) },
                            singleLine = true,
                            textStyle = AppTextStyles.SearchStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isSearchFocused = focusState.isFocused
                                    if (focusState.isFocused) {
                                        onGetHistory()
                                    }
                                }
                        ) { innerTextField ->
                            if (query.text.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search),
                                    style = AppTextStyles.SearchStyle,
                                    color = YP_Text_Gray,
                                )
                            }
                            innerTextField()
                        }
//                        TextField(
//                            value = query,
//                            onValueChange = {
//                                query = it
//                                onQueryChange(it.text)
//                            },
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .weight(1f),
//                            placeholder = { Text(text = stringResource(R.string.search)) },
//                            singleLine = true,
//                            textStyle = AppTextStyles.SearchStyle,
//                            colors = TextFieldDefaults.colors()
//                        )

                        // Очистка
                        if (query.text.isNotEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.vector_close_clear),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        query = TextFieldValue("")
                                        onClearText()
                                    },
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        // Показываем историю только если поиск в фокусе
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
                                        style = AppTextStyles.YouFindStyle
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                TrackList(state.trackList, onTrackClick)
                                Spacer(modifier = Modifier.height(8.dp))
                                ClearHistoryButton { onClearHistory() }
//                            Button(onClick = { onClearHistory() }) {
//                                Text("Clear History")
//                            }
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
//                                Text("No history available")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ClearHistoryButton(onClearHistory: () -> Unit) {
//        var isVisible by remember { mutableStateOf(false) } // управление видимостью
//
//        if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { onClearHistory() }
            ) {
                Text(
                    text = "Очистить историю",
                    fontSize = 16.sp // пример настройки стиля текста
                )
            }
        }
//        }
    }

    @Composable
    fun TrackList(tracks: List<TrackSearchDomain>, onTrackClick: (TrackSearchDomain) -> Unit) {
        LazyColumn {
            items(tracks) { track ->
                TrackItem(track, onTrackClick)
            }
        }
    }

    @Composable
    fun TrackItem(track: TrackSearchDomain, onTrackClick: (TrackSearchDomain) -> Unit) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onTrackClick(track) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Арт
            Card(
                modifier = Modifier
                    .width(45.dp)  // замените на dimension, если хотите брать из @dimen
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

            // Текстовая часть
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
//                                modifier = Modifier.weight(1f),
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

            // Стрелка
            Image(
                painter = painterResource(id = R.drawable.vector_arrow_forward),
                contentDescription = null,
                modifier = Modifier
                    .width(24.dp)  // замените на @dimen/track_view_image_view_arrow_forward_width
                    .height(24.dp),
                alignment = Alignment.Center,
            )
        }
    }


    @Composable
    fun ErrorScreen(error: SearchViewModel.ErrorSearch, onRetry: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val (messageRes, iconRes, showRetry) = when (error) {
                SearchViewModel.ErrorSearch.NOT_FOUND -> Triple(
                    "Nothing found",
                    R.drawable.nothing_found,
                    false
                )

                SearchViewModel.ErrorSearch.CONNECTION_PROBLEMS -> Triple(
                    "Connection problem",
                    R.drawable.connection_problems,
                    true
                )

                SearchViewModel.ErrorSearch.INVISIBLE -> Triple("", 0, false)
            }

            if (iconRes != 0) {
                Image(painter = painterResource(iconRes), contentDescription = null)
            }
            Text(messageRes)
            if (showRetry) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel: SearchViewModel by viewModel<SearchViewModel>()

        return ComposeView(requireContext()).apply {
            setContent {
                // Подписываемся на LiveData
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val analytics = Firebase.analytics
//
//        adapter = TrackAdapter(
//            onItemClickListener = { track -> onTrackClickDebounce(track) },
//            showDialog = null
//        )
//
//        onTrackClickDebounce = debounce<TrackSearchDomain>(
//            delayMillis = CLICK_DEBOUNCE_DELAY,
//            coroutineScope = viewLifecycleOwner.lifecycleScope,
//            useLastParam = false,
//        ) { track ->
//            viewModel.addTrackListHistory(track)
//
//            val bundle = Bundle().apply {
//                putString(FirebaseAnalytics.Param.ITEM_ID, track.trackId.toString())
//                putString(FirebaseAnalytics.Param.ITEM_NAME, track.trackName)
//                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "track")
//            }
//            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
//            Log.d("текущее состояние_трек", "${track.artistName}")
//            findNavController().navigate(
//                R.id.action_searchFragment_to_audioPlayerFragment,
//                AudioPlayerFragment.createArgs(track)
//                 )
//        }
//
//        binding.clearIcon.setOnClickListener {
//            binding.inputEditText.setText(DEFAULT_VALUE)
//            val inputMethodManager =
//                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
//            binding.layoutSearchError.isVisible = false
//        }
//
//        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                if (binding.inputEditText.text.isEmpty()) {
//                    viewModel.getHistoryList()
//                } else {
//                    showHistoryEmpty()
//                }
//            }
//        }
//
//        binding.buttonUpdateErrorSearch.setOnClickListener {
//            viewModel.request(requestText = requestText)
//            binding.errorImage.isVisible = false
//            binding.errorMessage.isVisible = false
//            binding.buttonUpdateErrorSearch.isVisible = false
//            showLoading()
//        }
//
//        val simpleTextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                if (s.isNullOrEmpty()) {
//                    binding.clearIcon.isVisible = false
//                    binding.layoutSearchError.isVisible = false
//                    viewModel.getHistoryList()
//                } else {
//                    showHistoryEmpty()
//                    requestText = s?.toString() ?: ""
//                    viewModel.searchRequestText(
//                        requestText = s?.toString() ?: ""
//                    )
//                    binding.clearIcon.isVisible = true
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        }
//
//        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
//
//        viewModel.observeSearchHistory().observe(viewLifecycleOwner) {
//            render(it)
//        }
//
//        binding.recyclerTrackView.adapter = adapter
//
//        binding.buttonClearHistory.setOnClickListener {
//            viewModel.clearHistoryList()
//            showHistoryEmpty()
//        }
//    }
//
//    private fun render(state: SearchHistoryState) {
//        when (state) {
//            is SearchHistoryState.SearchLoading -> showLoading()
//            is SearchHistoryState.SearchContent -> showContent(state.trackList)
//            is SearchHistoryState.SearchError -> showError(state.error)
//            is SearchHistoryState.HistoryContent -> showHistoryContent(state.trackList)
//            is SearchHistoryState.HistoryEmpty -> showHistoryEmpty()
//            is SearchHistoryState.HistoryClear -> showHistoryClear()
//        }
//    }
//
//    private fun showHistoryContent(trackList: List<TrackSearchDomain>) {
//        adapter?.clearOrUpdateTracks()
//        binding.headHistoryViews.isVisible = true
//        binding.recyclerTrackView.isVisible = true
//        binding.buttonClearHistory.isVisible = true
//        adapter?.allUpdateTracks(trackList)
//    }
//
//    private fun showHistoryEmpty() {
//        adapter?.clearOrUpdateTracks()
//        binding.headHistoryViews.isVisible = false
//        binding.recyclerTrackView.isVisible = false
//        binding.buttonClearHistory.isVisible = false
//    }
//
//    private fun showHistoryClear() {
//        binding.buttonClearHistory.setOnClickListener {
//            adapter?.clearOrUpdateTracks()
//            binding.headHistoryViews.isVisible = false
//            binding.buttonClearHistory.isVisible = false
//        }
//    }
//
//    private fun showLoading() {
//        binding.recyclerTrackView.isVisible = false
//        binding.progressBar.isVisible = true
//    }
//
//    private fun showError(error: SearchViewModel.ErrorSearch) {
//        when (error) {
//            SearchViewModel.ErrorSearch.NOT_FOUND -> {
//                binding.progressBar.isVisible = false
//                binding.layoutSearchError.isVisible = true
//                binding.errorImage.setImageResource(R.drawable.nothing_found)
//                binding.errorMessage.text = getString(R.string.error_search_nothing_found)
//                binding.buttonUpdateErrorSearch.isVisible = false
//            }
//
//            SearchViewModel.ErrorSearch.CONNECTION_PROBLEMS -> {
//                binding.progressBar.isVisible = false
//                binding.layoutSearchError.isVisible = true
//                binding.errorImage.isVisible = true
//                binding.errorMessage.isVisible = true
//                binding.errorImage.setImageResource(R.drawable.connection_problems)
//                binding.errorMessage.text = getString(R.string.error_search_connection_problems)
//                binding.buttonUpdateErrorSearch.isVisible = true
//            }
//
//            SearchViewModel.ErrorSearch.INVISIBLE -> {
//                binding.progressBar.isVisible = false
//                binding.layoutSearchError.isVisible = false
//            }
//        }
//    }
//
//    private fun showContent(trackList: List<TrackSearchDomain>) {
//        binding.headHistoryViews.isVisible = false
//        binding.buttonClearHistory.isVisible = false
//
//        binding.recyclerTrackView.isVisible = true
//        binding.progressBar.isVisible = false
//        adapter?.allUpdateTracks(trackList)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        internetAccessibility = InternetAccessibility()
//        requireContext().registerReceiver(
//            internetAccessibility,
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
//        )
//    }
//
//    override fun onPause() {
//        super.onPause()
//        internetAccessibility?.let {
//            requireContext().unregisterReceiver(internetAccessibility)
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        viewModel.saveSharedPrefs()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
    }

}
