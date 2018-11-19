package com.makeuseof.muocore.models;

/**
 * Created by Alisher Alikulov on 1/18/17.
 * for project makeuseof-android
 * with Android Studio
 */

public interface IPostsProviderListener {
    void firstPostsLoaded(int count);
    void newerPostsLoaded(int count, boolean isError, String errorMessage);
    void olderPostsLoaded(int count, boolean isError, String errorMessage, boolean firstLoad);
    void onNewPostsAvailable(int count);
    void onLoadingPostsFinish(boolean firstLoad, boolean older);
}
