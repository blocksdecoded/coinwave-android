package com.blocksdecoded.utils

import android.util.Log
import com.blocksdecoded.coinwave.BuildConfig

fun logD(text: String?, tag: String = "ololo") {
    Lg.d(text, tag)
}

fun logE(e: Exception, tag: String = "ololo") {
    Lg.e(e.message, e, tag)
}

fun logE(text: String?, e: Exception? = null, tag: String = "ololo") {
    Lg.e(text, e, tag)
}

object Lg {
    fun d(text: String?, tag: String = "ololo") {
        if (BuildConfig.DEBUG) {
            text?.let { Log.d(tag, it) }
        }
    }

    fun e(text: String?, e: Exception? = null, tag: String = "ololo") {
        if (BuildConfig.DEBUG) {
            text?.let { Log.e(tag, it, e) }
        }
    }
}