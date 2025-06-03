package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding

class NewPlayListFragment: Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNewPlaylist)

        binding.toolbarNewPlaylist.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}