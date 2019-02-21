package com.blocksdecoded.utils.permission

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.blocksdecoded.utils.logD
import com.blocksdecoded.utils.logE
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Created by askar on 12/7/18
 * with Android Studio
 */
object PermissionUtil {

    //region Public

    fun requestPermission(
        activity: Activity,
        permission: String,
        title: String,
        description: String,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
        onDialog: (dialog: Dialog) -> Unit
    ) {
        if (checkPermissionExist(activity, arrayListOf(permission))) {
            onGranted.invoke()
        } else {
            showDescriptionDialog(
                activity,
                title,
                description,
                onPositive = {
                    Dexter.withActivity(activity)
                        .withPermission(permission)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                onGranted.invoke()
                            }

                            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                                token?.continuePermissionRequest()
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                onDenied.invoke()
                                response?.also {
                                    if (it.isPermanentlyDenied) {
                                        showSettingsDialog(
                                            activity,
                                            title,
                                            description
                                        )?.also(onDialog)
                                    }
                                }
                            }
                        })
                        .onSameThread()
                        .withErrorListener {
                            logE(it.name)
                        }
                        .check()
                },
                onNegative = onDenied
            )?.also(onDialog)
        }
    }

    fun requestPermissions(
        activity: Activity,
        permissions: ArrayList<String>,
        title: String,
        description: String,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
        onDialog: (dialog: Dialog) -> Unit
    ) {
        if (checkPermissionExist(activity, permissions)) {
            onGranted.invoke()
        } else {
            showDescriptionDialog(
                activity,
                title,
                description,
                onPositive = {
                    Dexter.withActivity(activity)
                        .withPermissions(permissions)
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>?,
                                token: PermissionToken?
                            ) {
                                logD(permissions.toString())
                                token?.continuePermissionRequest()
                            }

                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                report?.grantedPermissionResponses?.forEach {
                                    logD("Granted permission ${it.permissionName}")
                                }

                                report?.deniedPermissionResponses?.forEach {
                                    logD("Denied permission ${it.permissionName}")
                                }
                                report?.also {
                                    if (it.areAllPermissionsGranted()) {
                                        onGranted.invoke()
                                    }
                                }
                            }
                        })
                        .onSameThread()
                        .withErrorListener {
                            logE(it.name)
                        }
                        .check()
                },
                onNegative = onDenied
            )?.also(onDialog)
        }
    }

    //endregion

    //region Private

    private fun checkPermissionExist(activity: Activity, permissions: ArrayList<String>): Boolean = try {
        var granted = true
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
                granted = false
            }
        }
        granted
    } catch (e: Exception) {
        false
    }

    private fun showDescriptionDialog(
        activity: Activity,
        title: String,
        description: String,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ): Dialog? = try {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage("$description\n\nThis app needs permission to use this feature.")
        builder.setPositiveButton("Ok") { dialog, which ->
            onPositive.invoke()
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            onNegative.invoke()
            dialog.cancel()
        }
        builder.show()
    } catch (e: Exception) {
        null
    }

    private fun showSettingsDialog(activity: Activity, title: String, description: String): Dialog? = try {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage("$description\n\nThis app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings(activity)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    } catch (e: Exception) {
        null
    }

    // navigating user to app settings
    private fun openSettings(activity: Activity) = try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 101)
    } catch (e: Exception) {
    }

    //endregion
}