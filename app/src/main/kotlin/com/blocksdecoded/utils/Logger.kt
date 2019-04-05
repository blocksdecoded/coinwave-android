package com.blocksdecoded.utils

import android.util.Log
import androidx.annotation.NonNull
import timber.log.Timber

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
    fun init(isDebug: Boolean) = if (isDebug) {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + ": " + element.lineNumber
            }
        })
    } else {
        Timber.plant(ReleaseLogTree())
    }

    fun logD(message: String) {
        Timber.d(message)
    }

    fun logE(e: Exception) {
        Timber.e(e)
    }

    fun logE(t: Throwable) {
        Timber.e(t)
    }

    private class ReleaseLogTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            @NonNull message: String,
            throwable: Throwable?
        ) {
            if (priority == Log.DEBUG || priority == Log.VERBOSE || priority == Log.INFO) {
                return
            }

            if (priority == Log.ERROR) {
                if (throwable == null) {
                    Timber.e(message)
                } else {
                    Timber.e(throwable, message)
                }
            }
        }
    }
}