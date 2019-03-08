package com.blocksdecoded.utils.customtabs

import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.fragment.app.Fragment
import com.blocksdecoded.coinwave.R

/**
 * Created by askar on 2/15/19
 * with Android Studio
 */

fun Fragment.openUrl(url: String) {
    activity?.openUrl(url)
}

fun Context.openUrl(url: String) {
    CustomTabsIntent
        .Builder()
        .setToolbarColor(Color.WHITE)
        .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        .setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)
        .build()
        .launchUrl(this, Uri.parse(url))
}

object CustomTabsUtil {

    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"

    fun bindToService(context: Context, connection: CustomTabsServiceConnection) = try {
        CustomTabsClient.bindCustomTabsService(context, CUSTOM_TAB_PACKAGE_NAME, connection)
    } catch (e: Exception) {
    }

    fun unbindFromService() {
    }
}