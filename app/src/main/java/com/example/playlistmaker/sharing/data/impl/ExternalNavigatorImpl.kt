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

class ExternalNavigatorImpl(val applicationContext: Context) : ExternalNavigator {

    companion object {
        private const val MIME_TYPE = "text/plain"
        private const val URI_STRING = "mailto:"
    }


    override fun shareLink() {

        val shareAppLink = getShareAppLink()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        applicationContext.startActivity(Intent.createChooser(shareIntent, chooseApp()))

    }

    override fun openLink() {

        val termsLink = getTermsLink()

        val url: Uri = Uri.parse(termsLink)
        val openLink = Intent(Intent.ACTION_VIEW).apply {
            data = url
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        applicationContext.startActivity(openLink)
    }

    override fun openEmail() {

        val emailData = getEmailData()
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(emailData.uri)
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        applicationContext.startActivity(Intent.createChooser(shareIntent, chooseApp()))
    }

    private fun getShareAppLink(): String {
        val link = ShareAppLink(applicationContext.getString(R.string.message_share_settings))
        return link.url
    }

    private fun getTermsLink(): String {
        val link = TermsLink(applicationContext.getString(R.string.uri_agreement_setting))
        return link.url
    }

    private fun getEmailData(): EmailData {
        return EmailData(
            email = arrayOf(applicationContext.getString(R.string.uri_support_setting)),
            subject = applicationContext.getString(R.string.theme_support_settings),
            text = applicationContext.getString(R.string.message_support_settings),
            uri = URI_STRING
        )
    }

    private fun chooseApp(): String {
        return applicationContext.getString(R.string.select_app)
    }
}
