package com.makeuseof.muocore.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.makeuseof.muocore.R;

/**
 * Created by Alisher Alikulov on 3/3/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class DialogUtil {
    public static ProgressDialog showProgress(Context context, String title){
        ProgressDialog progress=new ProgressDialog(context, R.style.ProgressDialog);
        progress.setMessage(title);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        return progress;
    }
}
