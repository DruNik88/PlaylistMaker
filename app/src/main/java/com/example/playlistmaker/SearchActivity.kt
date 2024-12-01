package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.api.ItunesApi
import com.example.playlistmaker.api.ItunesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        const val SAVE_VALUE = "SAVE_VALUE"
        const val DEFAULT_VALUE = ""
        const val BASEURL = "https://itunes.apple.com"
        const val ZERO = 0
    }

    private var inputValue: String = DEFAULT_VALUE

    private val iTunesBaseUrl = BASEURL
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var clearButton: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var layoutSearchError: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var buttonUpdateErrorSearch: Button
    private lateinit var headHistoryViews: TextView
    private lateinit var userHistory: TrackListHistory
    private lateinit var buttonClearHistory: Button


    private var trackListResponse = mutableListOf<Track>()
    private var trackListHistory = mutableListOf<Track>()

    private val adapter = TrackAdapter { track -> handlerTap(track) }

    private fun handlerTap(track: Track) {
        userHistory.addTrackListHistory(track)
        dataAudioPlayerActivity(track)
    }

    private fun dataAudioPlayerActivity(track: Track) {
        intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", track)
        val t = intent.getParcelableExtra<Track>("track")
        Log.d("t", "crash = $t")
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
                trackListHistory = userHistory.getListHistory()
                if (trackListHistory.isEmpty()) {
                    hideViewHistory()
                } else {
                    showViewHistory()
                    adapter.tracks.clear()
                    adapter.tracks.addAll(trackListHistory)
                    adapter.notifyDataSetChanged()
                    clearButtonHistory()
                }
            }

            State.TRACK_LIST -> {
                hideViewHistory()
                inputEditText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (inputEditText.text.isNotEmpty()) {
                            requestTrack(inputEditText.text.toString())
                        }
                        true
                    }
                    false
                }
            }
        }
    }

    private fun showViewHistory() {
        headHistoryViews.isVisible = true
        buttonClearHistory.isVisible = true
    }

    private fun hideViewHistory() {
        headHistoryViews.isVisible = false
        buttonClearHistory.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearButtonHistory() {
        buttonClearHistory.setOnClickListener {
            userHistory.clearHistory()
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
            if (trackListHistory.isEmpty()) {
                hideViewHistory()
            }
        }
    }

    private fun requestTrack(searchInput: String) {
        itunesService.search(searchInput).enqueue(object :
            Callback<ItunesResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ItunesResponse>,
                response: Response<ItunesResponse>
            ) {
                if (response.code() == 200) {
                    if ((response.body()?.resultCount ?: 0) > 0) {
                        val trackList = response.body()?.results ?: arrayListOf()
                        if (trackList.isNotEmpty()) {

                            trackListResponse = trackList

                            searchProblems(ErrorSearch.INVISIBLE)
                            adapter.tracks.clear()

                            adapter.tracks.addAll(filterTrackList(trackList))
                            adapter.notifyDataSetChanged()

                        }
                    } else {
                        searchProblems(ErrorSearch.NOT_FOUND)
                    }
                } else {
                    searchProblems(ErrorSearch.NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                searchProblems(ErrorSearch.CONNECTION_PROBLEMS)
            }
        })
    }

    private fun filterTrackList(tracList: MutableList<Track>): List<Track> {
        val filterTrackList: List<Track> = tracList
            .filter { track ->
                !track.trackName.isNullOrEmpty() &&
                        !track.artistName.isNullOrEmpty() &&
                        track.trackTimeMillis > ZERO
            }
        return filterTrackList
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun searchProblems(errorSearch: ErrorSearch) {
        if (adapter.tracks.isNotEmpty()) {
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
        }
        when (errorSearch) {
            ErrorSearch.NOT_FOUND -> {
                layoutSearchError.isVisible = true
                errorImage.setImageResource(R.drawable.nothing_found)
                errorMessage.text = getString(R.string.error_search_nothing_found)
                buttonUpdateErrorSearch.isVisible = false
            }

            ErrorSearch.CONNECTION_PROBLEMS -> {
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

        setSupportActionBar(toolbar)

        userHistory = TrackListHistory(applicationContext)

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
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
            displayedList(State.TRACK_HISTORY_LIST)
        }


        buttonUpdateErrorSearch.setOnClickListener {
            requestTrack(inputEditText.text.toString())
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
                        adapter.tracks.clear()
                        adapter.notifyDataSetChanged()
                        displayedList(State.TRACK_HISTORY_LIST)
                    }
                } else {
                    inputValue = s.toString()
                    clearButton.isVisible = true
                    if (adapter.tracks.isNotEmpty()) {
                        adapter.tracks.clear()
                        adapter.notifyDataSetChanged()
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
        userHistory.saveSharedPrefs(trackListHistory)
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
