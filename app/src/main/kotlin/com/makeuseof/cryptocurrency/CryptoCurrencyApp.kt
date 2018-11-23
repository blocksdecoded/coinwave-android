package com.makeuseof.cryptocurrency

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

// Created by askar on 6/7/18.
class CryptoCurrencyApp: Application() {

    companion object {
        var INSTANCE: Application? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        INSTANCE = this
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

//        Fabric.with(this, Crashlytics())
    }
}