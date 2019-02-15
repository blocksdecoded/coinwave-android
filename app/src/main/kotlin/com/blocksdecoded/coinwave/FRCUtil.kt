package com.blocksdecoded.coinwave

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Created by askar on 2/15/19
 * with Android Studio
 */
object FRCUtil {
    private const val CONFIG_COINS_URLS = "coins_urls"

    fun initConfigs() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaults(R.xml.remote_config_defaults)

        remoteConfig.fetch()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteConfig.activateFetched()
                    }

                    Log.d("ololo", remoteConfig.getString(CONFIG_COINS_URLS))
                }
    }
}