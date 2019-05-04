package com.blocksdecoded.utils

import android.util.Log

fun logD(message: String) {
    Logger.logD(message)
}

fun logE(e: Exception) {
    Logger.logE(e)
}

fun logE(t: Throwable) {
    Logger.logE(t)
}

object Logger {
    private const val TAG = "logger"
    private var debuggable = true

    fun init(isDebug: Boolean) {
        debuggable = isDebug
    }

    fun logD(message: String) {
        if (debuggable) {
            Log.d(TAG, message)
        }
    }

    fun logE(e: Exception) {
        if (debuggable) {
            e.message?.let {
                Log.e(TAG, e.message, e)
            } ?: Log.e(TAG, "", e)
        }
    }

    fun logE(t: Throwable) {
        if (debuggable) {
            t.message?.let {
                Log.e(TAG, t.message, t)
            } ?: Log.e(TAG, "", t)
        }
    }
}