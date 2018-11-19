package com.makeuseof.muocore.ui.listeners;

/**
 * Created by Alisher Alikulov on 3/13/17.
 * for project makeuseof-android
 * with Android Studio
 */

public interface ProgressListener {
    void onFail();
    void onProgress(float max, float current);
    void onCompleted();
}
