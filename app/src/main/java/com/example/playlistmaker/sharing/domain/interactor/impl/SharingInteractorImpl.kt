package com.example.playlistmaker.sharing.domain.interactor.impl

import android.content.Context
import com.example.playlistmaker.application.App
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }
}
//
//class SharingInteractorImpl(
//    private val externalNavigator: ExternalNavigator,
//) : SharingInteractor {
//    override fun shareApp(appInstance: Context) {
//        externalNavigator.shareLink(appInstance)
//    }
//
//    override fun openTerms(appInstance: Context) {
//        externalNavigator.openLink(appInstance)
//    }
//
//    override fun openSupport(appInstance: Context) {
//        externalNavigator.openEmail(appInstance)
//    }
//}