package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.model.ShareAppLink
import com.example.playlistmaker.sharing.domain.model.TermsLink

class ExternalNavigatorImpl() : ExternalNavigator {

    companion object {
        private const val MIME_TYPE = "text/plain"
        private const val URI_STRING = "mailto:"
    }


    override fun shareLink(appInstance: Context) {

        val shareAppLink = getShareAppLink(appInstance)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appInstance.startActivity(Intent.createChooser(shareIntent, chooseApp(appInstance)))

    }

    override fun openLink(appInstance: Context) {

        val termsLink = getTermsLink(appInstance)

        val url: Uri = Uri.parse(termsLink)
        val openLink = Intent(Intent.ACTION_VIEW).apply {
            data = url
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appInstance.startActivity(openLink)
    }

    override fun openEmail(appInstance: Context) {

        val emailData = getEmailData(appInstance)
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Log.e("data", "$data")
        }
        appInstance.startActivity(Intent.createChooser(shareIntent, chooseApp(appInstance)))

    }

    private fun getShareAppLink(appInstance: Context): String {
        val link = ShareAppLink(appInstance.getString(R.string.message_share_settings))
        return link.url
    }

    private fun getTermsLink(appInstance: Context): String {
        val link = TermsLink(appInstance.getString(R.string.uri_agreement_setting))
        return link.url
    }

    private fun getEmailData(appInstance: Context): EmailData {
        return EmailData(
            email = arrayOf(appInstance.getString(R.string.uri_support_setting)),
            subject = appInstance.getString(R.string.theme_support_settings),
            text = appInstance.getString(R.string.message_support_settings),
            uri = URI_STRING
        )
    }

    private fun chooseApp(appInstance: Context): String {
        return appInstance.getString(R.string.select_app)
    }
}
