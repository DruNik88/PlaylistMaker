package com.example.playlistmaker.settings.ui.fragment

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.application.compose.theme.Yp_Blue
import com.example.playlistmaker.application.compose.theme.Yp_Blue_Light
import com.example.playlistmaker.application.compose.theme.Yp_Gray
import com.example.playlistmaker.application.compose.theme.Yp_Gray_Light
import com.example.playlistmaker.settings.ui.state.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

@Composable
fun ThemeSwitcher(
    viewModel: SettingsViewModel
) {
    val themeState by viewModel.observeStateSearch().observeAsState()

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


@Composable
fun SwitchMaterialClone(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

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

        Box(
            modifier = Modifier
                .align(Alignment.Center) // чтобы по центру "сидел"
                .width(32.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) Yp_Blue_Light else Yp_Gray_Light)
        )

        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) Yp_Blue else Yp_Gray)
        )
    }
}

