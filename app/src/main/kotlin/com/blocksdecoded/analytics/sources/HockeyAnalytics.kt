package com.blocksdecoded.analytics.sources

import com.blocksdecoded.utils.logE
import net.hockeyapp.android.metrics.MetricsManager

// Created by askar on 11/16/18.
object HockeyAnalytics: AnalyticsContract {
    override fun addCustomEvent(eventName: String, options: HashMap<String, String>) = try {
        if (options.size > 0) {
            MetricsManager.trackEvent(eventName, options)
        } else {
            MetricsManager.trackEvent(eventName)
        }
    } catch (e: Exception) {
        logE(e)
    }

    override fun addInviteEvent(options: HashMap<String, String>) {
    }

    override fun addShareEvent(options: HashMap<String, String>) {
    }
}