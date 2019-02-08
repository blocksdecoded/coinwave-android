package com.blocksdecoded.analytics

import com.blocksdecoded.analytics.sources.FabricAnalytics
import com.blocksdecoded.analytics.sources.GoogleAnalytics
import com.blocksdecoded.analytics.sources.HockeyAnalytics
import com.blocksdecoded.utils.logE

/**
 * Created by Tameki on 2/5/18.
 */
object MainAnalytics {
	fun getBaseOptions(vararg values: Pair<String, String>): HashOptions<String, String> {
        val options = HashOptions<String, String>()

        values.forEach {
            options[it.first] = it.second
        }

        return options
    }

	fun addCustomEvent(eventName: String, options: HashMap<String, String> = getBaseOptions()) = try {
        FabricAnalytics.addCustomEvent(eventName, options)
        GoogleAnalytics.addCustomEvent(eventName, options)
        HockeyAnalytics.addCustomEvent(eventName, options)
    } catch (e: Exception) {
        logE(e)
    }
	
	fun inviteEvent(options: HashMap<String, String> = getBaseOptions()) = try {
        FabricAnalytics.addInviteEvent(options)
        GoogleAnalytics.addInviteEvent(options)
        HockeyAnalytics.addInviteEvent(options)
	} catch (e: Exception) {
        logE(e)
    }
	
	fun shareEvent(options: HashMap<String, String> = getBaseOptions()) = try {
        FabricAnalytics.addShareEvent(options)
        GoogleAnalytics.addShareEvent(options)
        HockeyAnalytics.addShareEvent(options)
	} catch (e: Exception) {
        logE(e)
    }
}