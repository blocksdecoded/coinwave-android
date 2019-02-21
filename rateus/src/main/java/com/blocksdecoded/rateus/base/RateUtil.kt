package com.blocksdecoded.rateus.base

import android.content.Context
import com.blocksdecoded.rateus.util.SharedPrefsUtil
import com.blocksdecoded.rateus.util.SharedPrefsUtil.setPreference
import com.blocksdecoded.rateus.util.TimeUtil

/**
 * Created by Tameki on 3/20/18.
 */

object RateUtil {
    val LAST_SUBMITTED_RATING = "last_submitted_rating"
    private val LAST_CHECK_DATE = "last_rate_date"

    fun checkRateShow(
        context: Context?,
        appName: String = context?.applicationInfo?.name.toString(),
        appID: String = context?.packageName.toString()
    ): RateUsDialogContract? {
        context?.let {
            if (checkDate(it) && checkRating(it)) {
                return showRate(it, appName, appID)
            }
        }

        return null
    }

    fun showRate(
        context: Context,
        appName: String = context.applicationInfo.name,
        appID: String = context.packageName
    ): RateUsDialogContract {
        val dialog = RateUsDialog(context, mAppName = appName, mAppID = appID)
        dialog.show()
        return dialog
    }

    private fun checkDate(context: Context): Boolean {
        val date = SharedPrefsUtil.getStringPreference(context, LAST_CHECK_DATE)

        if (date.isEmpty()) {
            setPreference(context, LAST_CHECK_DATE, TimeUtil.getFormattedDate())

            return false
        } else {
            try {
                if (TimeUtil.getDaysBetween(date) >= 3) {
                    setPreference(context, LAST_CHECK_DATE, TimeUtil.getFormattedDate())
                    return true
                }
            } catch (e: Exception) {
            }
        }

        return false
    }

    private fun checkRating(context: Context): Boolean =
            SharedPrefsUtil.getFloatPreference(context, LAST_SUBMITTED_RATING) == 0f
}