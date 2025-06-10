package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment: Fragment(){

    companion object{
        fun newInstance() = PlayListFragment()
    }

    val viewModel: PlayListViewModel by viewModel<PlayListViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var adapter: PlayListAdapter = PlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylist.observe(viewLifecycleOwner){ playListData->
            when(playListData){
                is PlayListData.Content -> {
                    val playList = playListData.playListData
                    showContent(playList)
                }
                is PlayListData.Empty -> showPlaceHolder()
            }
        }

        binding.recyclerPlayList.adapter = adapter

        binding.buttonNewPlaylist.setOnClickListener{
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlayListFragment
            )
        }
    }

    private fun showPlaceHolder(){
        binding.errorImage.isVisible = true
        binding.errorMessage.isVisible =true
    }

    private fun showContent(playlist: List<PlayList>){
        adapter.clearOrUpdatePlayList()
        binding.errorImage.isVisible = false
        binding.errorMessage.isVisible =false
        binding.recyclerPlayList.isVisible = true
        adapter.allUpdatePlayList(playlist)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}