package com.makeuseof.muocore.util;

import android.content.Context;

/**
 * Created by Alisher Alikulov on 12/15/16.
 * for project makeuseof-android
 * with Android Studio
 */

public class DimenUtils {
    private final Context context;

    public DimenUtils(Context context){
        this.context = context;
    }

    public float pxToDp(float px){
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }

    public int dpToPx(float dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
