package com.makeuseof.muocore.util;

import android.util.Log;

/**
 * Created by Alisher Alikulov on 1/24/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class MyDebug {
    public static void e(String message){
        Log.e("MUO_ERROR_LOG", message);
    }

    public static void i(String message){
        Log.i("MUO_INFO_LOG", message);
    }

    public static void d(String message){
        Log.d("MUO_DEBUG_LOG", message);
    }
}
