package com.makeuseof.muocore.ui.delegates;

import com.makeuseof.muocore.context.MuoCoreContext;

/**
 * Created by Alisher Alikulov on 2/21/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class PostUrlSchemaDelegate {
    public static String getBase(String url) {
        int endIndex = url.indexOf("/", 10);
        if (endIndex < 1) {
            return url;
        }
        return url.substring(0, endIndex);
    }

    public boolean isUrlFromSite(String url){
        return url.startsWith(getBase(MuoCoreContext.getInstance().getSiteUrl()));
    }

    public boolean isPostUrl(String url) {
        return false;
    }

    public boolean isAuthorsPage(String url) {
        return false;
    }

    public String getPostSlug(String url) {
        return url;
    }
}
