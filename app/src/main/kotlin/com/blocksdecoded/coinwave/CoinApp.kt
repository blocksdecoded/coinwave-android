package com.blocksdecoded.coinwave

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blocksdecoded.coinwave.di.coinApp
import com.blocksdecoded.utils.Logger
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

class CoinApp : Application() {

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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
        INSTANCE = this
        startKoin(this, coinApp)

        Logger.init(BuildConfig.DEBUG)

        Fabric.with(this, Crashlytics())
    }
}