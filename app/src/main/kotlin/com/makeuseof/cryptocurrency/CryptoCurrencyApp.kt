package com.makeuseof.cryptocurrency

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.makeuseof.muocore.context.MuoCoreContext
import com.makeuseof.muocore.util.MuoUrlSchemaUtil
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
        initMuoCore()
    }

    private fun initMuoCore() {
        val apiUrl = BuildConfig.API_POST_URL
        MuoCoreContext.init(apiUrl, applicationContext)
        MuoCoreContext.getInstance().apiAccessId = ""
        MuoCoreContext.getInstance().apiSecretKey = ""
        MuoCoreContext.getInstance().isMuoApp = true
        MuoCoreContext.getInstance().siteUrl = "http://www.makeuseof.com"
//        MuoCoreContext.getInstance().coreBaseActivityListener = CoreActivityListenerImpl()
        MuoCoreContext.getInstance().postUrlSchemaDelegate = MuoUrlSchemaUtil()
        MuoCoreContext.getInstance().setNeedXAccessKey(false)
        MuoCoreContext.getInstance().defaultPlaceholder = R.drawable.default_bg
    }
}