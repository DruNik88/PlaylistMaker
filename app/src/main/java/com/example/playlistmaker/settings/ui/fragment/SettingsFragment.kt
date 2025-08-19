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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.state.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import androidx.compose.runtime.livedata.observeAsState

class SettingsFragment : Fragment() {

    @SuppressLint("IntentReset", "QueryPermissionsNeeded")
    @Composable
    fun SettingsItem(text: String, icon: Int, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }

    @Composable
    fun SwitchMaterialClone(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        // смещение кружка: 0.dp -> (32 - 18).dp = 14.dp
        val thumbOffset by animateDpAsState(
            targetValue = if (checked) 14.dp else 0.dp,
            label = ""
        )

        Box(
            modifier = Modifier
                .width(32.dp)   // общая ширина
                .height(18.dp)  // подгоняем под диаметр круга
                .clickable { onCheckedChange(!checked) },
            contentAlignment = Alignment.CenterStart
        ) {
            // Track (паз)
            Box(
                modifier = Modifier
                    .align(Alignment.Center) // чтобы по центру "сидел"
                    .width(32.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (checked) Color(0xFF6200EE) else Color(0xFFBDBDBD))
            )

            // Thumb (кружок)
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsScreen(
        onShareClick: () -> Unit,
        onSupportClick: () -> Unit,
        onUserAgreementClick: () -> Unit,
        viewModel: SettingsViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Toolbar
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Switch (под SwitchMaterial)
            var isDarkTheme by remember { mutableStateOf(false) }
//            ThemeSwitcher(viewModel)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { isDarkTheme = !isDarkTheme },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dark_team),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                ThemeSwitcher(viewModel)
            }

            // Share the app
            SettingsItem(
                text = stringResource(id = R.string.share_the_app),
                icon = R.drawable.vector_share,
                onClick = onShareClick
            )

            // Write to support
            SettingsItem(
                text = stringResource(id = R.string.write_to_support),
                icon = R.drawable.vector_support,
                onClick = onSupportClick
            )

            // User agreement
            SettingsItem(
                text = stringResource(id = R.string.user_agreement),
                icon = R.drawable.vector_arrow_forward,
                onClick = onUserAgreementClick
            )
        }

    }

    @Composable
    fun ThemeSwitcher(
        viewModel: SettingsViewModel
    ) {
        // Подписываемся на LiveData как состояние Compose
        val themeState by viewModel.observeStateSearch().observeAsState()

        // Определяем состояние для Switch
        val isChecked = when (themeState) {
            is SettingsState.LightMode -> (themeState as SettingsState.LightMode).light
            is SettingsState.DarkMode -> (themeState as SettingsState.DarkMode).dark
            else -> false
        }

        SwitchMaterialClone(
            checked = isChecked,
            onCheckedChange = { checked ->
                viewModel.getSwitchTheme(checked)
            }
        )

    }


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
                val onSupportClick: () -> Unit = { viewModel.openSupport(requireContext()) }
                val onUserAgreementClick: () -> Unit = { viewModel.openTerms(requireContext()) }

                SettingsScreen(
                    onShareClick = onShareClick,
                    onSupportClick = onSupportClick,
                    onUserAgreementClick = onUserAgreementClick,
                    viewModel = viewModel
                )


            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        viewModel.observeStateSearch().observe(viewLifecycleOwner) { theme ->
//            binding.themeSwitcher.setOnCheckedChangeListener(null)
//
//            when (theme) {
//                is SettingsState.LightMode -> binding.themeSwitcher.isChecked = theme.light
//                is SettingsState.DarkMode -> binding.themeSwitcher.isChecked = theme.dark
//            }
//
//            binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
//                viewModel.getSwitchTheme(checked)
//            }
//
//        }
//
//        binding.shareTheApp.setOnClickListener {
//            viewModel.shareApp(requireContext())
//        }
//
//        binding.writeToSupport.setOnClickListener {
//            viewModel.openSupport(requireContext())
//        }
//
//        binding.userAgreement.setOnClickListener {
//            viewModel.openTerms(requireContext())
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}

