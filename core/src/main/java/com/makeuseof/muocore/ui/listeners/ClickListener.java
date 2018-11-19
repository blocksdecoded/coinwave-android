package com.makeuseof.muocore.ui.listeners;

import android.view.View;

/**
 * Created by Alisher Alikulov on 11/8/16.
 * for project makeuseof-android
 * with Android Studio
 */


public interface ClickListener {
    void onClick(View v, int position);
    void onLongClick(View v, int position);
}
