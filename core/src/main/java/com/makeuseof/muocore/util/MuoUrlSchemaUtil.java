package com.makeuseof.muocore.util;

import com.makeuseof.muocore.ui.delegates.PostUrlSchemaDelegate;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class MuoUrlSchemaUtil extends PostUrlSchemaDelegate {
    private static final String POST_BASE_URL = "http://www.makeuseof.com/tag";
    private static final String AUTHORS_BASE_URL = "//api.makeuseof.com/authors";
    private static final String BASE_URL = "http://www.makeuseof.com";

    public boolean isPostUrl(String url) {
        return url.startsWith(POST_BASE_URL);
    }

    public boolean isAuthorsPage(String url) {
        return url.contains(AUTHORS_BASE_URL);
    }

    public String getPostSlug(String url) {
        return url.substring(POST_BASE_URL.length()).trim().replace("/", "");
    }
}
