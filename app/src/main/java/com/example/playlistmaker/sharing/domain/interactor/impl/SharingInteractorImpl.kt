package com.example.playlistmaker.sharing.domain.interactor.impl

import android.content.Context
import com.example.playlistmaker.application.App
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(appInstance: Context) {
        externalNavigator.shareLink(appInstance)
    }

    override fun openTerms(appInstance: Context) {
        externalNavigator.openLink(appInstance)
    }

    override fun openSupport(appInstance: Context) {
        externalNavigator.openEmail(appInstance)
    }

//    private fun getShareAppLink(): String {
//        return (R.string.message_share_settings).toString()
//    }
//
//    private fun getSupportEmailData(): EmailData {
//        // Нужно реализовать
//    }
//
//    private fun getTermsLink(): String {
//        // Нужно реализовать
//    }
}