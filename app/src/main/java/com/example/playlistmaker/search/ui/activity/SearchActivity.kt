package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackList


class SearchActivity : AppCompatActivity() {

    enum class ErrorSearch {
        NOT_FOUND,
        CONNECTION_PROBLEMS,
        INVISIBLE
    }

    enum class State {
        TRACK_LIST,
        TRACK_HISTORY_LIST
    }

    companion object {
        const val NOT_FOUND = 1
        const val SAVE_VALUE = "SAVE_VALUE"
        const val DEFAULT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val getTrackList = Creator.provideGetTrackListInteractor()

    private var inputValue: String = DEFAULT_VALUE
    private var isClickAllowed = true

    private lateinit var clearButton: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var layoutSearchError: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var buttonUpdateErrorSearch: Button
    private lateinit var headHistoryViews: TextView
    private lateinit var buttonClearHistory: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var getUserHistory: HistoryInteractor

    private var trackListResponse = TrackList(list = mutableListOf())
    private var trackListHistory = TrackList(list = mutableListOf())

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private val adapter = TrackAdapter { track -> if (clickDebounce()) handlerTap(track) }

    private fun handlerTap(track: TrackSearchDomain) {
        getUserHistory.addTrackListHistory(track)
        dataAudioPlayerActivity(track)
    }

    private fun dataAudioPlayerActivity(track: TrackSearchDomain) {
        intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", track)
        startActivity(intent)
    }

    private fun definitionState() {
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) displayedList(State.TRACK_HISTORY_LIST)
            else displayedList(State.TRACK_LIST)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayedList(stateList: State) {
        when (stateList) {
            State.TRACK_HISTORY_LIST -> {
                searchDebounce(State.TRACK_HISTORY_LIST)
                adapter.clearOrUpdateTracks()
                trackListHistory = getUserHistory.getListHistory()
                if (trackListHistory.list.isEmpty()) {
                    hideViewHistory()
                } else {
                    showViewHistory()
                    adapter.allUpdateTracks(trackListHistory)
                    clearButtonHistory()
                }
            }

            State.TRACK_LIST -> {
                hideViewHistory()
                searchDebounce(State.TRACK_LIST)
            }
        }
    }

    private fun showViewHistory() {
        headHistoryViews.isVisible = true
        recyclerView.isVisible = true
        buttonClearHistory.isVisible = true
    }

    private fun hideViewHistory() {
        headHistoryViews.isVisible = false
        buttonClearHistory.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearButtonHistory() {
        buttonClearHistory.setOnClickListener {
            getUserHistory.clearHistory()
            adapter.clearOrUpdateTracks()
            trackListHistory.list.clear()
            if (trackListHistory.list.isEmpty()) {
                hideViewHistory()
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { requestTrack() }

    private fun searchDebounce(stateList: State) {
        when (stateList) {
            State.TRACK_LIST -> {
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            }

            State.TRACK_HISTORY_LIST -> handler.removeCallbacks(searchRunnable)
        }
    }

    private fun requestTrack() {

        recyclerView.isVisible = false
        progressBar.isVisible = true

        getTrackList.searchTrackList(
            expression = inputEditText.text.toString(),
            consumer = object : TrackListInteractor.TrackListConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(trackList: Resource<List<TrackSearchDomain>>) {
                    handler.post {
                        when (trackList) {
                            is Resource.Success -> {

                                recyclerView.isVisible = true
                                progressBar.isVisible = false

                                trackListResponse = TrackList(list = trackList.data.toMutableList())

                                searchProblems(ErrorSearch.INVISIBLE)

                                adapter.allUpdateTracks(trackListResponse)
                            }

                            is Resource.Error -> {
                                if (trackList.message == NOT_FOUND) {

                                    progressBar.isVisible = false

                                    Log.d("trackList.message", "${trackList.message}")
                                    searchProblems(ErrorSearch.NOT_FOUND)
                                } else {

                                    progressBar.isVisible = false

                                    Log.d("trackList.message", "${trackList.message}")
                                    searchProblems(ErrorSearch.CONNECTION_PROBLEMS)
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchProblems(errorSearch: ErrorSearch) {
        if (adapter.tracks.isNotEmpty()) {
            adapter.clearOrUpdateTracks()
        }
        when (errorSearch) {
            ErrorSearch.NOT_FOUND -> {
                layoutSearchError.isVisible = true
                errorImage.setImageResource(R.drawable.nothing_found)
                errorMessage.text = getString(R.string.error_search_nothing_found)
                buttonUpdateErrorSearch.isVisible = false
            }

            ErrorSearch.CONNECTION_PROBLEMS -> {
                progressBar.isVisible = false
                layoutSearchError.isVisible = true
                errorImage.setImageResource(R.drawable.connection_problems)
                errorMessage.text = getString(R.string.error_search_connection_problems)
                buttonUpdateErrorSearch.isVisible = true
            }

            else -> layoutSearchError.isVisible = false
        }
    }


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        clearButton = findViewById(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        recyclerView = findViewById(R.id.recyclerTrackView)
        toolbar = findViewById(R.id.toolbar_search)
        layoutSearchError = findViewById(R.id.layoutSearchError)
        errorImage = findViewById(R.id.errorImage)
        errorMessage = findViewById(R.id.errorMessage)
        buttonUpdateErrorSearch = findViewById(R.id.buttonUpdateErrorSearch)
        headHistoryViews = findViewById(R.id.headHistoryViews)
        buttonClearHistory = findViewById(R.id.buttonClearHistory)
        progressBar = findViewById(R.id.progressBar)

        setSupportActionBar(toolbar)

        getUserHistory = Creator.provideGetHistoryInteractor()

        toolbar.setNavigationIcon(R.drawable.vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        if (inputValue.isNotEmpty()) {
            inputEditText.setText(inputValue)
        }

        clearButton.setOnClickListener {
            inputEditText.setText(DEFAULT_VALUE)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            layoutSearchError.isVisible = false
            definitionState()
        }

        buttonUpdateErrorSearch.setOnClickListener {
            requestTrack()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (inputEditText.hasFocus()) {
                    if (s.isNullOrEmpty()) {
                        displayedList(State.TRACK_HISTORY_LIST)
                    } else {
                        displayedList(State.TRACK_LIST)
                    }
                }
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                    layoutSearchError.isVisible = false
                    if (adapter.tracks.isNotEmpty()) {
                        adapter.clearOrUpdateTracks()
                        displayedList(State.TRACK_HISTORY_LIST)
                    }
                } else {
                    inputValue = s.toString()
                    clearButton.isVisible = true
                    if (adapter.tracks.isNotEmpty()) {
                        adapter.clearOrUpdateTracks()
                        displayedList(State.TRACK_LIST)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        recyclerView.adapter = adapter

        definitionState()
    }

    override fun onStop() {
        super.onStop()
        getUserHistory.saveSharedPrefs()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_VALUE, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(SAVE_VALUE, DEFAULT_VALUE)
    }
}
