package com.blocksdecoded.rateus.util

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

/**
 * Created by Tameki on 3/24/18.
 */

internal fun Context.getFont(@FontRes font: Int): Typeface? = ResourceUtil.getTypeface(this, font)

internal fun Context.getColorRes(@ColorRes color: Int): Int = ResourceUtil.getColor(this, color)

internal object ResourceUtil {
    fun getColor(context: Context, @ColorRes color: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(color, null)
    } else {
        context.resources.getColor(color)
    }

    fun getTypeface(context: Context, @FontRes font: Int): Typeface? = ResourcesCompat.getFont(context, font)
}