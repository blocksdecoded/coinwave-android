package com.makeuseof.analytics.sources

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.makeuseof.utils.logE


// Created by askar on 11/15/18.
object GoogleAnalytics : AnalyticsContract {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    fun initAnalytics(context: Context){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun addCustomEvent(eventName: String, options: HashMap<String, String>) {
        try {
            val bundle = Bundle().apply {
                options.keys.forEach {
                    putString(it, options[it])
                }
            }
            mFirebaseAnalytics?.logEvent(eventName, bundle)
        } catch (e: Exception) {
            logE(e)
        }
    }

    override fun addInviteEvent(options: HashMap<String, String>) {
    }

    override fun addShareEvent(options: HashMap<String, String>) {
    }
}