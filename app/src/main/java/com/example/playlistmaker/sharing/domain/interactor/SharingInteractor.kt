package com.example.playlistmaker.sharing.domain.interactor

import android.content.Context

interface SharingInteractor {
    fun shareApp(context: Context)
    fun openTerms(context: Context)
    fun openSupport(context: Context)
}
