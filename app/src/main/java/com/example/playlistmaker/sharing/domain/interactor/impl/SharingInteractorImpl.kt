package com.example.playlistmaker.sharing.domain.interactor.impl

import android.content.Context
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(context: Context) {
        externalNavigator.shareLink(context)
    }

    override fun openTerms(context: Context) {
        externalNavigator.openLink(context)
    }

    override fun openSupport(context: Context) {
        externalNavigator.openEmail(context)
    }
}

