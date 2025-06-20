package com.example.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.application.TrackAdapter
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val DEFAULT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var adapter: TrackAdapter<TrackSearchDomain>? = null
    private lateinit var onTrackClickDebounce: (TrackSearchDomain) -> Unit
    private var requestText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(
            onItemClickListener = { track -> onTrackClickDebounce(track) },
            showDialog = null
        )

        onTrackClickDebounce = debounce<TrackSearchDomain>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
        ) { track ->
            viewModel.addTrackListHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText(DEFAULT_VALUE)
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            binding.layoutSearchError.isVisible = false
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (binding.inputEditText.text.isEmpty()) {
                    viewModel.getHistoryList()
                } else {
                    showHistoryEmpty()
                }
            }
        }

        binding.buttonUpdateErrorSearch.setOnClickListener {
            viewModel.request(requestText = requestText)
            binding.errorImage.isVisible = false
            binding.errorMessage.isVisible = false
            binding.buttonUpdateErrorSearch.isVisible = false
            showLoading()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {
                    binding.clearIcon.isVisible = false
                    binding.layoutSearchError.isVisible = false
                    viewModel.getHistoryList()
                } else {
                    showHistoryEmpty()
                    requestText = s?.toString() ?: ""
                    viewModel.searchRequestText(
                        requestText = s?.toString() ?: ""
                    )
                    binding.clearIcon.isVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        viewModel.observeSearchHistory().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.recyclerTrackView.adapter = adapter

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistoryList()
            showHistoryEmpty()
        }
    }

    private fun render(state: SearchHistoryState) {
        when (state) {
            is SearchHistoryState.SearchLoading -> showLoading()
            is SearchHistoryState.SearchContent -> showContent(state.trackList)
            is SearchHistoryState.SearchError -> showError(state.error)
            is SearchHistoryState.HistoryContent -> showHistoryContent(state.trackList)
            is SearchHistoryState.HistoryEmpty -> showHistoryEmpty()
            is SearchHistoryState.HistoryClear -> showHistoryClear()
        }
    }

    private fun showHistoryContent(trackList: List<TrackSearchDomain>) {
        adapter?.clearOrUpdateTracks()
        binding.headHistoryViews.isVisible = true
        binding.recyclerTrackView.isVisible = true
        binding.buttonClearHistory.isVisible = true
        adapter?.allUpdateTracks(trackList)
    }

    private fun showHistoryEmpty() {
        adapter?.clearOrUpdateTracks()
        binding.headHistoryViews.isVisible = false
        binding.recyclerTrackView.isVisible = false
        binding.buttonClearHistory.isVisible = false
    }

    private fun showHistoryClear() {
        binding.buttonClearHistory.setOnClickListener {
            adapter?.clearOrUpdateTracks()
            binding.headHistoryViews.isVisible = false
            binding.buttonClearHistory.isVisible = false
        }
    }

    private fun showLoading() {
        binding.recyclerTrackView.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showError(error: SearchViewModel.ErrorSearch) {
        when (error) {
            SearchViewModel.ErrorSearch.NOT_FOUND -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = true
                binding.errorImage.setImageResource(R.drawable.nothing_found)
                binding.errorMessage.text = getString(R.string.error_search_nothing_found)
                binding.buttonUpdateErrorSearch.isVisible = false
            }

            SearchViewModel.ErrorSearch.CONNECTION_PROBLEMS -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = true
                binding.errorImage.isVisible = true
                binding.errorMessage.isVisible = true
                binding.errorImage.setImageResource(R.drawable.connection_problems)
                binding.errorMessage.text = getString(R.string.error_search_connection_problems)
                binding.buttonUpdateErrorSearch.isVisible = true
            }

            SearchViewModel.ErrorSearch.INVISIBLE -> {
                binding.progressBar.isVisible = false
                binding.layoutSearchError.isVisible = false
            }
        }
    }

    private fun showContent(trackList: List<TrackSearchDomain>) {
        binding.headHistoryViews.isVisible = false
        binding.buttonClearHistory.isVisible = false

        binding.recyclerTrackView.isVisible = true
        binding.progressBar.isVisible = false
        adapter?.allUpdateTracks(trackList)

    }

    override fun onStop() {
        super.onStop()
        viewModel.saveSharedPrefs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
