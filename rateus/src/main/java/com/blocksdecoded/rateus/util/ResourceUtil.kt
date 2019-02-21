package com.blocksdecoded.rateus.util

import android.content.Context
import android.os.Build

/**
 * Created by Tameki on 3/24/18.
 */
internal object ResourceUtil {
    fun getColor(context: Context, color: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.resources.getColor(color, null)
        } else {
            return context.resources.getColor(color)
        }
    }
}