// package com.makeuseof.core
//
// import android.os.Bundle
// import android.support.v7.app.AppCompatActivity
// import android.content.pm.PackageManager
// import com.makeuseof.invoiceapp.BuildConfig
// import net.hockeyapp.android.CrashManager
// import net.hockeyapp.android.CrashManagerListener
// import net.hockeyapp.android.UpdateManager
//
//
// // Created by askar on 6/6/18.
// open class BaseHockeyActivity: AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        checkForUpdates()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        checkForCrashes()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        unregisterManagers()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterManagers()
//    }
//
//    private fun checkForCrashes() {
//        if (BuildConfig.DEBUG){
//            CrashManager.register(this)
//        } else {
//            try {
//                val app = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
//                val bundle = app.metaData
//                val appId = bundle.getString("net.hockeyapp.android.appIdentifier")
//                CrashManager.register(this, appId, object : CrashManagerListener() {
//                    override fun shouldAutoUploadCrashes(): Boolean {
//                        return true
//                    }
//                })
//            } catch (e: Exception) {
// //                Lg.d(e.message)
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun checkForUpdates() {
//        // Remove this for store builds!
//        if (BuildConfig.DEBUG){
//            UpdateManager.register(this)
//        }
//    }
//
//    private fun unregisterManagers() {
//        if (BuildConfig.DEBUG){
//            UpdateManager.unregister()
//        }
//    }
// }