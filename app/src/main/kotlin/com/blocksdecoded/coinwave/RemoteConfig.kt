package com.blocksdecoded.coinwave

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Created by askar on 2/15/19
 * with Android Studio
 */
object RemoteConfig {
    private val CONFIG_COINS_URL = "coins_urls"

    fun initConfigs() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaults(R.xml.remote_config_defaults)
        remoteConfig.fetch()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteConfig.activateFetched()
                    }

                    Log.d("ololo", remoteConfig.getString(CONFIG_COINS_URL))
                }
    }
}