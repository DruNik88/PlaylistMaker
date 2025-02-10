package com.example.playlistmaker.sharing.data

import android.content.Context

interface ExternalNavigator {
    fun shareLink(context: Context)
    fun openLink(context: Context)
    fun openEmail(context: Context)
}

