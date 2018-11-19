package com.makeuseof.muocore.ui.delegates;

import android.content.Context;

import com.makeuseof.muocore.backend.interfaces.IPost;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class StatsDelegate {
    public boolean isUnread(IPost post){return false;}
    public void postRead(Context context, IPost post) {}
    public void markPostAsRead(IPost post) {}
}
