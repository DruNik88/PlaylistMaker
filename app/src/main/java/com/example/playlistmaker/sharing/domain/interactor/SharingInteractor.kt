package com.example.playlistmaker.sharing.domain.interactor

import android.content.Context
import com.example.playlistmaker.application.App

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
}

//interface SharingInteractor {
//    fun shareApp(appInstance: Context)
//    fun openTerms(appInstance: Context)
//    fun openSupport(appInstance: Context)
//}