package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import com.example.playlistmaker.search.ui.state.HistoryState
import com.example.playlistmaker.search.ui.state.SearchState
import com.example.playlistmaker.search.ui.view_model.SeachViewModel
import com.example.playlistmaker.search.ui.view_model.SeachViewModel.ErrorSearch


class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SeachViewModel>()

    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val DEFAULT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private var requestText: String = ""

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private val adapter = TrackAdapter { track ->
        if (clickDebounce()) {
            viewModel.addTrackListHistory(track)
            intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeStateSearch().observe(this) { state ->
            render(state)
        }

        setSupportActionBar(binding.toolbarSearch)
        binding.toolbarSearch.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText(DEFAULT_VALUE)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            binding.layoutSearchError.isVisible = false
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty()) viewModel.getHistoryList()
            else viewModel.searchRequestText(
                requestText = requestText
            )
        }

        binding.buttonUpdateErrorSearch.setOnClickListener {
            viewModel.searchRequestText(requestText = requestText)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.inputEditText.hasFocus()) {
                    if (s.isNullOrEmpty()) {
                        viewModel.getHistoryList()
                    } else {
                        viewModel.searchRequestText(
                            requestText = s?.toString() ?: ""
                        )
                    }
                }

                if (s.isNullOrEmpty()) {
                    binding.clearIcon.isVisible = false
                    binding.layoutSearchError.isVisible = false
                    if (adapter.tracks.isNotEmpty()) {
                        adapter.clearOrUpdateTracks()
                        viewModel.getHistoryList()
                    }
                } else {
                    requestText = s?.toString() ?: ""
                    viewModel.searchRequestText(
                        requestText = requestText
                    )
                    binding.clearIcon.isVisible = true
                    if (adapter.tracks.isNotEmpty()) {
                        showHistoryEmpty()
                        viewModel.searchRequestText(
                            requestText = s?.toString() ?: ""
                        )
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        viewModel.observeStateSearch().observe(this) {
            render(it)
        }

        viewModel.observeHistorySearch().observe(this) {
            renderHistory(it)
        }

        binding.recyclerTrackView.adapter = adapter

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistoryList()
            showHistoryEmpty()
        }
    }

    private fun renderHistory(historyState: HistoryState) {
        when (historyState) {
            is HistoryState.Content -> showHistoryContent(historyState.trackList)
            HistoryState.Empty -> showHistoryEmpty()
            HistoryState.Clear -> showHistoryClear()
        }
    }

    private fun showHistoryContent(trackList: TrackSearchListDomain) {
        adapter.clearOrUpdateTracks()
        binding.headHistoryViews.isVisible = true
        binding.recyclerTrackView.isVisible = true
        binding.buttonClearHistory.isVisible = true
        adapter.allUpdateTracks(trackList)
    }

    private fun showHistoryEmpty() {
        adapter.clearOrUpdateTracks()
        binding.headHistoryViews.isVisible = false
        binding.buttonClearHistory.isVisible = false
    }

    private fun showHistoryClear() {
        binding.buttonClearHistory.setOnClickListener {
            adapter.clearOrUpdateTracks()
            binding.headHistoryViews.isVisible = false
            binding.buttonClearHistory.isVisible = false

        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.trackList)
            is SearchState.Error -> showError(state.error)
        }
    }

    private fun showLoading() {
        binding.recyclerTrackView.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showError(error: ErrorSearch) {
        when (error) {
            ErrorSearch.NOT_FOUND -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = true
                binding.errorImage.setImageResource(R.drawable.nothing_found)
                binding.errorMessage.text = getString(R.string.error_search_nothing_found)
                binding.buttonUpdateErrorSearch.isVisible = false
            }

            ErrorSearch.CONNECTION_PROBLEMS -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = true
                binding.errorImage.setImageResource(R.drawable.connection_problems)
                binding.errorMessage.text = getString(R.string.error_search_connection_problems)
                binding.buttonUpdateErrorSearch.isVisible = true
            }

            ErrorSearch.INVISIBLE -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = false
            }
        }
    }

    private fun showContent(trackList: TrackSearchListDomain) {
        binding.recyclerTrackView.isVisible = true
        binding.progressBar.isVisible = false
        adapter.allUpdateTracks(trackList)

    }

    override fun onStop() {
        super.onStop()
        viewModel.saveSharedPrefs()
    }
}
