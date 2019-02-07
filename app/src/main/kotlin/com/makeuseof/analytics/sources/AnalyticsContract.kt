package com.makeuseof.analytics.sources

// Created by askar on 11/15/18.
interface AnalyticsContract {
    fun addCustomEvent(eventName: String, options: HashMap<String, String>)

    fun addInviteEvent(options: HashMap<String, String>)

    fun addShareEvent(options: HashMap<String, String>)
}