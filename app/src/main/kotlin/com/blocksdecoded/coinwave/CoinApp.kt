package com.blocksdecoded.coinwave

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blocksdecoded.coinwave.di.coinApp
import com.blocksdecoded.utils.Logger
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.android.startKoin

// Created by askar on 6/7/18.
class CoinApp : Application() {

    companion object {
        var INSTANCE: Application? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        INSTANCE = this
        MultiDex.install(this)
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
        INSTANCE = this
        startKoin(this, coinApp)

        Logger.setup(BuildConfig.DEBUG)

//        FirebaseApp.initializeApp(this)

//        Fabric.with(this, Crashlytics())
    }
}