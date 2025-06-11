package com.example.playlistmaker.settings.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.state.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    @SuppressLint("IntentReset", "QueryPermissionsNeeded")

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeStateSearch().observe(viewLifecycleOwner) { theme ->
            binding.themeSwitcher.setOnCheckedChangeListener(null)

            when (theme) {
                is SettingsState.LightMode -> binding.themeSwitcher.isChecked = theme.light
                is SettingsState.DarkMode -> binding.themeSwitcher.isChecked = theme.dark
            }

            binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.getSwitchTheme(checked)
            }

        }

        binding.shareTheApp.setOnClickListener {
            viewModel.shareApp(requireContext())
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport(requireContext())
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openTerms(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

