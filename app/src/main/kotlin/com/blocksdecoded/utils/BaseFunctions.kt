package com.blocksdecoded.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.Toast
import com.blocksdecoded.utils.extensions.config

fun showShortToast(context: Context?, text: String) = showToast(context, text, Toast.LENGTH_SHORT)

fun showLongToast(context: Context?, text: String) = showToast(context, text, Toast.LENGTH_LONG)

private fun showToast(context: Context?, text: String, length: Int) {
    context?.let {
        Toast.makeText(it, text, length).show()
    }
}

fun isAppIsInBackground(context: Context): Boolean {
    var isInBackground = true
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
    } else {
        val taskInfo = am.getRunningTasks(1)
        val componentInfo = taskInfo[0].topActivity
        if (componentInfo.packageName == context.packageName) {
            isInBackground = false
        }
    }

    return isInBackground
}