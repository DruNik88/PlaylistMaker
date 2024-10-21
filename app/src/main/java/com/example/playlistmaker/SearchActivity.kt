package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
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

    companion object {
        const val SAVE_VALUE = "SAVE_VALUE"
        const val DEFAULT_VALUE = ""
        const val BASEURL = "https://itunes.apple.com"
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
    private lateinit var trackList: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var layoutSearchError: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var buttonUpdateErrorSearch: Button

    private val adapter = TrackAdapter(mutableListOf())

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
                        searchProblems(ErrorSearch.INVISIBLE)
                        adapter.tracks.clear()
                        adapter.tracks.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun searchProblems(errorSearch: ErrorSearch) {
        if (adapter.tracks.isNotEmpty()) {
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
        }
        when (errorSearch) {
            ErrorSearch.NOT_FOUND -> {
                layoutSearchError.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.nothing_found)
                errorMessage.text = getString(R.string.error_search_nothing_found)
                buttonUpdateErrorSearch.visibility = View.GONE
            }

            ErrorSearch.CONNECTION_PROBLEMS -> {
                layoutSearchError.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.connection_problems)
                errorMessage.text = getString(R.string.error_search_connection_problems)
                buttonUpdateErrorSearch.visibility = View.VISIBLE
            }

            else -> layoutSearchError.visibility = View.GONE
        }
    }


    @SuppressLint("ServiceCast", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        clearButton = findViewById(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        trackList = findViewById(R.id.recyclerTrackView)
        toolbar = findViewById(R.id.toolbar_search)
        layoutSearchError = findViewById(R.id.layoutSearchError)
        errorImage = findViewById(R.id.errorImage)
        errorMessage = findViewById(R.id.errorMessage)
        buttonUpdateErrorSearch = findViewById(R.id.buttonUpdateErrorSearch)
        setSupportActionBar(toolbar)

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
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
        }

        buttonUpdateErrorSearch.setOnClickListener {
            requestTrack(inputEditText.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    inputValue = s.toString()
                    clearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        trackList.adapter = adapter

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_VALUE, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(SAVE_VALUE, DEFAULT_VALUE)
    }

}
