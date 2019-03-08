package com.blocksdecoded.utils.extensions

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

val Context.density
    get() = resources.displayMetrics.density

val Context.scaledDensity
    get() = resources.displayMetrics.scaledDensity

val Context.screenHeight
    get() = screenSize.y

val Context.screenWidth
    get() = screenSize.x

val Context.screenSize: Point
    get() = Point().apply {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getSize(this)
    }

val Context.statusBarHeight: Int
    get() = try {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            resources.getDimensionPixelSize(resourceId)
        else
            dpToPx(if (VERSION.SDK_INT >= VERSION_CODES.M) 24 else 25)
    } catch (e: Exception) {
        0
    }

fun Context.dpToPx(dp: Int) = (dp * density).toInt()

fun Context.pxToDp(px: Int) = (px / density).toInt()

fun Context.pxToSp(px: Float) = px / scaledDensity

fun Context.spToPx(sp: Float) = sp * scaledDensity

fun Context.getColorRes(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)