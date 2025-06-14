package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.medialibrary.ui.viewmodel.PlayListInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListInfoFragment: Fragment() {

    val viewModel: PlayListInfoViewModel by viewModel<PlayListInfoViewModel>()

    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

//    private var adapter: PlayListAdapter = PlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
