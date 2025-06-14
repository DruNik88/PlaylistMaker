package com.example.playlistmaker.medialibrary.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import java.io.File

class NewPlayListFragment : Fragment() {

    val viewModel: NewPlayListViewModel by viewModel<NewPlayListViewModel>()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    val requestPermission = PermissionRequester.instance()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private fun toolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNewPlaylist)

        binding.toolbarNewPlaylist.setNavigationIcon(R.drawable.vector_arrow_back)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            handleBackPressed()
        }
    }

    private fun handleBackPressed(
        shouldDisableCallback: Boolean = false,
        callback: OnBackPressedCallback? = null
    ) {
        val hasUnsavedChanges =
            binding.titleNewPlaylist.text?.toString()?.trim()?.isNotEmpty() ?: false ||
                    binding.descriptionNewPlaylist.text?.toString()?.trim()
                        ?.isNotEmpty() ?: false ||
                    !binding.addPhotoNewPlaylist.isVisible

        if (hasUnsavedChanges) {
            val show = confirmDialog.show()
            val ypBlue = ContextCompat.getColor(requireContext(), R.color.yp_blue)
            show.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ypBlue)
            show.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ypBlue)
        } else {
            if (shouldDisableCallback) {
                callback?.isEnabled = false
            }
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun namePlaylist(): String {
        val title = binding.titleNewPlaylist.text
        return getString(R.string.playlist_created, title)
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
            viewModel.updateTitle(newTitle = text.toString())
        }

        viewModel.title.observe(viewLifecycleOwner) { title ->
            val isNotEmpty = !title.isNullOrBlank()
            binding.buttonCreateNewPlaylist.isEnabled = isNotEmpty
            binding.titleNewPlaylistMiddle.isVisible = isNotEmpty
            if (binding.titleNewPlaylist.text.toString() != title)
                binding.titleNewPlaylist.setText(title)
            if (isNotEmpty) {
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.titleNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

        binding.descriptionNewPlaylist.addTextChangedListener { text ->
            viewModel.updateDescription(newDescription = text.toString())
        }

        viewModel.description.observe(viewLifecycleOwner) { description ->
            val isNotEmpty = !description.isNullOrBlank()
            binding.descriptionNewPlaylistMiddle.isVisible = isNotEmpty
            if (binding.descriptionNewPlaylist.text.toString() != description) {
                binding.descriptionNewPlaylist.setText(description)
            }
            if (isNotEmpty) {
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_blue)
            } else {
                binding.descriptionNewPlaylist.setBackgroundResource(R.drawable.ic_rectangle_gray)
            }
        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            viewModel.saveDataBase()
            Toast.makeText(requireContext(), namePlaylist(), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    viewModel.updateImageUri(uri)
                }
            }

        binding.artworkNewPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.imageLocalStoragePath.observe(viewLifecycleOwner) { imageLocalStoragePath ->
            val file = imageLocalStoragePath?.let { File(it) }
            if (file?.exists() == true) {
                binding.artworkNewPlaylist.setImageURI(Uri.fromFile(file))
                binding.addPhotoNewPlaylist.isVisible = false
            } else {
                binding.artworkNewPlaylist.setImageResource(R.drawable.ic_placeholder)
                binding.addPhotoNewPlaylist.isVisible = false
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_description)
            .setNeutralButton(R.string.cancel) { dialog, which ->
                // ничего не делаем
            }.setPositiveButton(R.string.complete) { dialog, which ->
                findNavController().navigateUp()
            }

        ((activity as AppCompatActivity)).onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed(
                    shouldDisableCallback = true,
                    callback = this
                )
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}