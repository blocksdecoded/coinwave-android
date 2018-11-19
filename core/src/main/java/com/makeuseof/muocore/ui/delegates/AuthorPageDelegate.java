package com.makeuseof.muocore.ui.delegates;

import android.app.Activity;
import android.content.Context;

import com.makeuseof.muocore.backend.interfaces.IPost;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class AuthorPageDelegate<P extends IPost> {
    public boolean canOpenAuthorPage(String url, P mPost) {
        return false;
    }

    public void openAuthorPage(Activity activity, String url, P mPost) {

    }

    public void postLoaded(Context context, P post) {

    }
}
