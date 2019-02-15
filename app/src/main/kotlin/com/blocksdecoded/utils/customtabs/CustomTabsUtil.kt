package com.blocksdecoded.utils.customtabs

import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.blocksdecoded.coinwave.R

/**
 * Created by askar on 2/15/19
 * with Android Studio
 */
object CustomTabsUtil {

    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"

    fun bindToService(context: Context, connection: CustomTabsServiceConnection) = try {
        CustomTabsClient.bindCustomTabsService(context, CUSTOM_TAB_PACKAGE_NAME, connection)
    } catch (e: Exception) {

    }

    fun unbindFromService() {

    }

    fun openUrl(
            context: Context,
            url: String
    ) {
        CustomTabsIntent
                .Builder()
                .setToolbarColor(Color.WHITE)
                .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
                .build()
                .launchUrl(context, Uri.parse(url))
    }

}