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
    private var mIsDebuggable = true

    fun setup(isDebuggable: Boolean) {
        mIsDebuggable = isDebuggable
    }

    fun logD(message: String, tag: String = TAG) {
        if (mIsDebuggable) {
            Log.d(tag, message)
        }
    }

    fun logE(e: Exception, tag: String = TAG) {
        if (mIsDebuggable) {
            Log.e(tag, e.message, e)
        }
    }
}