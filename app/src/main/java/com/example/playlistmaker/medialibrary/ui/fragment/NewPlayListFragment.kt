package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding

class NewPlayListFragment : Fragment() {

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

        binding.buttonCreateNewPlaylist.isEnabled = false


        binding.titleNewPlaylist.addTextChangedListener { text ->
            val isNotEmpty = !text.isNullOrBlank()
            binding.buttonCreateNewPlaylist.isEnabled = isNotEmpty
            binding.titleNewPlaylistMiddle.isVisible = isNotEmpty
            if (isNotEmpty){
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

        binding.descriptionNewPlaylist.addTextChangedListener { text ->
            val isNotEmpty = !text.isNullOrBlank()
            binding.descriptionNewPlaylistMiddle.isVisible = isNotEmpty
            if (isNotEmpty){
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}