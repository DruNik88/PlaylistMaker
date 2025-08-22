package com.example.playlistmaker.settings.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.playlistmaker.settings.ui.state.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.application.compose.theme.PlaylistMakerTheme
import com.example.playlistmaker.application.compose.theme.Yp_Blue
import com.example.playlistmaker.application.compose.theme.Yp_Blue_Light
import com.example.playlistmaker.application.compose.theme.Yp_Gray
import com.example.playlistmaker.application.compose.theme.Yp_Gray_Light
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

                val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

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