package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {

                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                val viewModel: SettingsViewModel = koinViewModel()

                val onShareClick: () -> Unit = { viewModel.shareApp(requireContext()) }
                val onSupportClick: () -> Unit =
                    { viewModel.openSupport(requireContext()) }
                val onUserAgreementClick: () -> Unit =
                    { viewModel.openTerms(requireContext()) }

                PlaylistMakerTheme {
                    SettingsScreen(
                        onShareClick = onShareClick,
                        onSupportClick = onSupportClick,
                        onUserAgreementClick = onUserAgreementClick,
                        viewModel = viewModel
                    )

                }
            }
        }
    }
}