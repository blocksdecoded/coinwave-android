package com.makeuseof.muocore.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Alisher Alikulov on 2/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class DeviceUtils {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
