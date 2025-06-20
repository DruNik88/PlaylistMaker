package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.viewmodel.NewPlayListRedactViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NewPlayListRedactFragment : NewPlayListFragment() {
    companion object {

        private const val PLAYLIST = "playlist"

        fun createArgs(playList: PlayList): Bundle {
            return bundleOf(PLAYLIST to playList)
        }
    }

    override val viewModel: NewPlayListRedactViewModel by viewModel<NewPlayListRedactViewModel>() {
        val playList = requireArguments().getSerializable(PLAYLIST) as? PlayList
        playList?.let {
            parametersOf(it)
        } ?: throw IllegalArgumentException("PlayList is null")
    }

    override fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNewPlaylist)

        binding.toolbarNewPlaylist.setTitle("Редактировать")
        binding.toolbarNewPlaylist.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar()

        binding.buttonCreateNewPlaylist.text = getString(R.string.save)

        viewModel.getPlayListLiveData().observe(viewLifecycleOwner) { playList ->

            playList.title?.let { showTitle(it) }
            playList.description?.let { showDescription(it) }
            playList.imageLocalStoragePath?.let { showImage(it) }

        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            viewModel.updatePlayList()
            findNavController().navigateUp()
        }

        ((activity as AppCompatActivity)).onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

}