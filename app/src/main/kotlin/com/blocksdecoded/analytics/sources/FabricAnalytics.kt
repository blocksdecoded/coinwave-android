package com.blocksdecoded.analytics.sources

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.answers.InviteEvent
import com.crashlytics.android.answers.ShareEvent
import com.blocksdecoded.utils.logE

/**
 * Created by Tameki on 2/5/18.
 */
object FabricAnalytics : AnalyticsContract {
    override fun addCustomEvent(eventName: String, options: HashMap<String, String>) = try {
        val event = CustomEvent(eventName)

        options.keys.forEach {
            event.putCustomAttribute(it, options[it])
        }

        Answers.getInstance().logCustom(event)
    } catch (e: Exception) {
        logE(e)
    }

    override fun addInviteEvent(options: HashMap<String, String>) {
        val event = InviteEvent()

        options.keys.forEach {
            event.putCustomAttribute(it, options[it])
        }

        Answers.getInstance().logInvite(event)
    }

    override fun addShareEvent(options: HashMap<String, String>) {
        val event = ShareEvent()

        options.keys.forEach {
            event.putCustomAttribute(it, options[it])
        }

        Answers.getInstance().logShare(event)
    }
}