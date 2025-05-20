package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.playlistmaker.databinding.FragmentFavouriteTrackBinding
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import com.example.playlistmaker.medialibrary.ui.viewmodel.FavouriteTrackViewModel

class FavouriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTrackFragment()
    }

    val viewModel by activityViewModels<FavouriteTrackViewModel>()

    private var _binding: FragmentFavouriteTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavouriteLiveData().observe(viewLifecycleOwner) { favouriteList ->
            when (favouriteList) {
                is FavouriteData.Content -> showFavouriteList(favouriteList)
                is FavouriteData.Empty -> showPlaceHolder()
            }
        }

    }

    private fun showPlaceHolder() {
        binding.errorImage.isVisible = true
        binding.errorMessage.isVisible = true
        binding.recyclerTrackView.isVisible = false
    }

    private fun showFavouriteList(favouriteList: FavouriteData) {
        binding.errorImage.isVisible = false
        binding.errorMessage.isVisible = false
        binding.recyclerTrackView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}