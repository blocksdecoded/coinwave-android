package com.blocksdecoded.utils

import android.util.Log

fun logD(message: String, tag: String = Logger.TAG) {
    Logger.logD(message, tag)
}

fun logE(e: Exception, tag: String = Logger.TAG) {
    Logger.logE(e, tag)
}

object Logger {
    val TAG = "ololo"
    private var isDebuggable = true

    fun setup(isDebuggable: Boolean) {
        this.isDebuggable = isDebuggable
    }

    fun logD(message: String, tag: String = TAG) {
        if (isDebuggable) {
            Log.d(tag, message)
        }
    }

    fun logE(e: Exception, tag: String = TAG) {
        if (isDebuggable) {
            Log.e(tag, e.message, e)
        }
    }
}