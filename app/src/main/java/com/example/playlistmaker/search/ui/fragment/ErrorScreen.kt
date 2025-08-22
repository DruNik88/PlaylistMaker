package com.example.playlistmaker.search.ui.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.application.compose.UpdateClearCreateButton
import com.example.playlistmaker.application.compose.theme.AppTextStyles
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun ErrorScreen(error: SearchViewModel.ErrorSearch, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val (iconRes, messageRes, showRetry) =
            when (error) {
                SearchViewModel.ErrorSearch.NOT_FOUND -> Triple(
                    first = R.drawable.nothing_found,
                    second = R.string.nothing_found,
                    third = false
                )

                SearchViewModel.ErrorSearch.CONNECTION_PROBLEMS -> Triple(
                    first = R.drawable.connection_problems,
                    second = R.string.error_search_connection_problems,
                    third = true
                )

                SearchViewModel.ErrorSearch.INVISIBLE -> Triple(0, 0, false)
            }

        if (iconRes != 0) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = stringResource(messageRes),
            style = AppTextStyles.SearchTextStyle,
            textAlign = TextAlign.Center
        )

        if (showRetry) {
            Spacer(modifier = Modifier.height(8.dp))
            UpdateClearCreateButton(stringResource(R.string.update)) { onRetry() }
        }
    }
}
