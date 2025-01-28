package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.application.App

interface ExternalNavigator {
    fun shareLink(appInstance: Context)
    fun openLink(appInstance: Context)
    fun openEmail(appInstance: Context)
}