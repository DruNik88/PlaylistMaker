package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.medialibrary.ui.viewmodel.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlayListFragment : Fragment() {

    val viewModel: NewPlayListViewModel by viewModel<NewPlayListViewModel>()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    val requestPermission = PermissionRequester.instance()

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNewPlaylist)

        binding.toolbarNewPlaylist.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            confirmDialog.show()
        }
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
            if (isNotEmpty) {
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

        binding.descriptionNewPlaylist.addTextChangedListener { text ->
            val isNotEmpty = !text.isNullOrBlank()
            binding.descriptionNewPlaylistMiddle.isVisible = isNotEmpty
            if (isNotEmpty) {
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback вызовется, когда пользователь выберет картинку
                if (uri != null) {
                    binding.artworkNewPlaylist.setImageURI(uri)
                    binding.addPhotoNewPlaylist.isVisible = false
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }

        binding.artworkNewPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setPositiveButton("Завершить") { dialog, which ->
                // сохраняем изменения и выходим
                // save()
//                (activity as AppCompatActivity).finish()
                findNavController().navigateUp()
            }

        ((activity as AppCompatActivity)).onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmDialog.show()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}