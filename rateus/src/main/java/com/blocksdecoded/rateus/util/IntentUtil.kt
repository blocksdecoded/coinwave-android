package com.blocksdecoded.rateus.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.blocksdecoded.rateus.BuildConfig

/**
 * Created by Tameki on 3/21/18.
 */
internal object IntentUtil {
    private val CONTACT_MAILS = arrayOf(BuildConfig.CONTACT_EMAIL)

    fun openAppPAge(activity: Activity, appID: String) {
        try {
            activity.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appID))
            )
        } catch (e: Exception) {
            activity.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appID))
            )
        }
    }

    fun contactUs(activity: Activity?, appName: String) {
        try {
            activity?.let {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, CONTACT_MAILS)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android $appName Feedback")
                if (intent.resolveActivity(it.packageManager) != null) {
                    it.startActivity(
                            Intent.createChooser(intent, "Send email..."))
                }
            }
        } catch (e: Exception) {
        }
    }
}