/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.makeuseof.muocore.context.MuoCoreContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Post Manager
 */

public class StylesheetsController {
    public final static String POST_STYLESHEETS = "post_stylesheets";
    public final static String CSS_FILENAME = "style.css";

    public static void saveStylesheets(Context context, String styleSheets) throws IOException {
        SharedPreferences prefs = MuoCoreContext.getInstance().getGlobalPrefs().getPreferences();
        if (prefs.contains(POST_STYLESHEETS)) {
            prefs.edit().remove(POST_STYLESHEETS).apply();
        }

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(CSS_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter dos = new OutputStreamWriter(fos);
            dos.write(styleSheets, 0, styleSheets.length());
            dos.flush();
            dos.close();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static File getCssFile(Context context) {
        return new File(context.getFilesDir(), CSS_FILENAME);
    }
}
