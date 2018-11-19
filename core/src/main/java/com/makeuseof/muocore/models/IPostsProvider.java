package com.makeuseof.muocore.models;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.backend.interfaces.IPost;

import java.util.List;

/**
 * Created by Alisher Alikulov on 1/18/17.
 * for project makeuseof-android
 * with Android Studio
 */

public interface IPostsProvider {
    boolean hasPosts();

    void addListener(IPostsProviderListener listener);

    void removeListener(IPostsProviderListener listener);

    void loadOlder(Context mContext, Integer mLastPostId, List<Integer> o, boolean firstLoad, boolean installedGrouvi);

    void loadNewer(Context mContext, List<Integer> mCategoryIds);

    IPost findByID(String postId);

    IPost getPostFromDB(String postId);

    IPost getRecentPost();

    IPost getCachedPost(String postId);

    IPost get(int position);

    boolean isSaved(String postId);

    void setRecentPost(IPost mPost);

    boolean isRecentPost(String postId);

    void clearPosts();

    int count();

    int getLastPostId();

    int getFirstPostId();

    String getSavedCSSFilePath(Context context);

    IPost mapPost(JsonNode resultNode) throws JsonProcessingException;

    List<Integer> getSubscribedCategoriesIds();

    boolean shouldFetchNewPosts();

    void checkNewPosts(Context context);

    void addExtraPost(IPost mPost);
}
