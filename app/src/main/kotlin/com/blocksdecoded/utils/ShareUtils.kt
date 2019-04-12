package com.blocksdecoded.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.R

object ShareUtils {
    private val CONTACT_MAILS = arrayOf(BuildConfig.CONTACT_EMAIL)
    private const val SHARE_LINK = BuildConfig.SHARE_LINK

    fun shareText(activity: Activity, text: String) {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, text)
            activity.startActivity(Intent.createChooser(i, "Share with"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareApp(activity: Activity?) {
        activity?.let {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.const_app_name)
            var content = "\nI think you'll love this app:\n"
            content += "$SHARE_LINK\n"
            i.putExtra(Intent.EXTRA_TEXT, content)
            activity.startActivity(Intent.createChooser(i, "Share with"))
        }
    }

    fun contactUs(activity: Activity?) {
        try {
            activity?.let {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, CONTACT_MAILS)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android " + it.getString(R.string.const_app_name) + " Feedback")
                if (intent.resolveActivity(it.packageManager) != null) {
                    it.startActivity(Intent.createChooser(intent, "Send email..."))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}