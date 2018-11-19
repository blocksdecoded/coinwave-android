package com.makeuseof.muocore.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.ApiRequest;
import com.makeuseof.muocore.backend.GetNewerPostsRequest;
import com.makeuseof.muocore.backend.GetPostsRequest;
import com.makeuseof.muocore.backend.GetStylesheetsRequest;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.ui.delegates.BookmarkDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alisher Alikulov on 1/24/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class MuoCorePostsProvider implements IPostsProvider {
    private List<IPostsProviderListener> listeners = new ArrayList<>();
    private List<IPost> posts = new ArrayList<>();
    private List<IPost> extraPosts = new ArrayList<>();
    private IPost recentPost = null;
    private boolean usingCachedPosts = true;
    private boolean stylesDownloaded = false;

    private MuoCorePostsProvider(){
        posts = MuoCachedPosts.getCachedPosts();
    }

    private void updateCachedPosts() {
        MuoCachedPosts.clearCachedPosts();
        MuoCachedPosts.cacheLastPosts(posts);
    }

    @Override
    public boolean hasPosts() {
        return posts.size() > 0;
    }

    @Override
    public void addListener(IPostsProviderListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(IPostsProviderListener listener){
        if(listeners.contains(listener)){
            this.listeners.remove(listener);
        }
    }

    public void addExtraPost(IPost mPost){
        extraPosts.add(mPost);
    }

    @Override
    public void loadOlder(final Context mContext, Integer mLastPostId, List<Integer> categoryIds, final boolean firstLoad, boolean installedGrouvi) {
        GetPostsRequest getPostsRequest = new GetPostsRequest(mContext, mLastPostId, categoryIds, firstLoad, installedGrouvi, null) {
            @Override
            public void onSuccess(List<IPost> loadedPosts) {
                MuoCorePostsProvider.this.posts.addAll(loadedPosts);
                for(IPostsProviderListener listener: listeners) {
                    listener.olderPostsLoaded(loadedPosts.size(), false, null, firstLoad);
                }
                updateCachedPosts();
            }

            @NonNull
            @Override
            public List<IPost> mapPosts(JsonNode resultNode) throws JsonProcessingException {
                ArrayList<Post> posts = new ArrayList<>(Arrays.asList(objectMapper.treeToValue(resultNode.get("posts"), Post[].class)));
                return Post.convertList(posts);
            }

            @Override
            public void onFinish() {
                for(IPostsProviderListener listener: listeners) {
                    listener.onLoadingPostsFinish(firstLoad, false);
                }
            }

            @Override
            public void onNoConnection() {
                for(IPostsProviderListener listener: listeners) {
                    listener.olderPostsLoaded(0, true, mContext.getString(R.string.no_internet_connection_or_server_unreachable), firstLoad);
                }
            }

            @Override
            public void onFailure(String message) {
                for(IPostsProviderListener listener: listeners) {
                    listener.olderPostsLoaded(0, true, message, firstLoad);
                }
            }
        };
        checkStylesheetsAndLoad(mContext, getPostsRequest);
    }

    @Override
    public void loadNewer(final Context mContext, List<Integer> mCategoryIds) {
        if(count() > 0) {
            GetNewerPostsRequest getNewerPostsRequest = new GetNewerPostsRequest(mContext, getFirstPostId(), mCategoryIds) {
                @Override
                public void onSuccess(List<IPost> loadedPosts) {
                    if(usingCachedPosts){
                        posts.clear();
                    }
                    posts.addAll(0, loadedPosts);
                    updateCachedPosts();
                    if(usingCachedPosts){
                        usingCachedPosts = false;
                        for(IPostsProviderListener listener: listeners) {
                            listener.firstPostsLoaded(loadedPosts.size());
                        }
                    }else{
                        for(IPostsProviderListener listener: listeners) {
                            listener.newerPostsLoaded(loadedPosts.size(), false, null);
                        }
                    }
                }

                @NonNull
                @Override
                public List<IPost> mapPosts(JsonNode resultNode) throws JsonProcessingException {
                    List<Post> posts = new ArrayList<>(Arrays.asList(objectMapper.treeToValue(resultNode.get("posts"), Post[].class)));
                    return Post.convertList(posts);
                }

                @Override
                public void onFinish() {
                    for(IPostsProviderListener listener: listeners) {
                        listener.onLoadingPostsFinish(false, true);
                    }
                }

                @Override
                public void onNoConnection() {
                    for(IPostsProviderListener listener: listeners) {
                        listener.newerPostsLoaded(0, true, mContext.getString(R.string.no_internet_connection_or_server_unreachable));
                    }
                }
            };
            checkStylesheetsAndLoad(mContext, getNewerPostsRequest);
        }
    }

    @Override
    public IPost mapPost(JsonNode resultNode) throws JsonProcessingException  {
        return ApiRequest.objectMapper.treeToValue(resultNode, Post.class);
    }

    @Override
    public List<Integer> getSubscribedCategoriesIds() {
        return new ArrayList<>();
    }

    @Override
    public boolean shouldFetchNewPosts() {
        return false;
    }

    @Override
    public void checkNewPosts(final Context context) {
        int firstPostId = posts.size() > 0 ? posts.get(0).getPostId() : 0;
        GetNewerPostsRequest getNewerPostsRequest = new GetNewerPostsRequest(context, firstPostId, getSubscribedCategoriesIds()) {
            @Override
            public void onSuccess(List<IPost> newPosts) {
                if(newPosts.size() > 0) {
                    if(MuoCoreContext.getInstance().isAutoRefreshOnHomePageEnabled()){
                        loadNewer(context, getSubscribedCategoriesIds());
                    }else{
                        for(IPostsProviderListener listener: listeners) {
                            listener.onNewPostsAvailable(newPosts.size());
                        }
                    }
                }
            }

            @NonNull
            @Override
            public List<IPost> mapPosts(JsonNode resultNode) throws JsonProcessingException {
                List<Post> posts = new ArrayList<>(Arrays.asList(objectMapper.treeToValue(resultNode.get("posts"), Post[].class)));
                return Post.convertList(posts);
            }

            @Override
            public void onFinish() {}

            @Override
            public void onNoConnection() {}
        };
        checkStylesheetsAndLoad(context, getNewerPostsRequest);
    }

    private void checkStylesheetsAndLoad(final Context mContext, final ApiRequest request) {
        if(stylesDownloaded){
            ApiRequest.apiClient.perform(request);
        }else {
            ApiRequest.apiClient.perform(new GetStylesheetsRequest(mContext) {

                @Override
                public void onSuccess(String html) {
                    try {
                        StylesheetsController.saveStylesheets(mContext, html);
                        stylesDownloaded = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    ApiRequest.apiClient.perform(request);
                }
            });
        }
    }

    public int getFirstPostId() {
        if(usingCachedPosts){
            return 0;
        }
        return posts.size() > 0 ? posts.get(0).getPostId() : 0;
    }

    @Override
    public String getSavedCSSFilePath(Context context) {
        return StylesheetsController.getCssFile(context).getAbsolutePath();
    }

    @Override
    public IPost findByID(String postId) {
        for(IPost post: posts){
            if(post.getPostId().toString().equals(postId)){
                return post;
            }
        }
        for(IPost post: extraPosts){
            if(post.getPostId().toString().equals(postId)){
                return post;
            }
        }
        BookmarkDelegate bookmarkDelegate = MuoCoreContext.getInstance().getBookmarkDelegate();
        if(bookmarkDelegate.isBookmarked(postId)){
            return bookmarkDelegate.getPostById(postId);
        }
        return null;
    }

    public List<IPost> getPosts(int max){
        List<IPost> list = new ArrayList<>();
        if(posts.size() < max){
            list.addAll(posts);
        }else{
            list.addAll(posts.subList(0, max));
        }
        return list;
    }

    @Override
    public IPost getPostFromDB(String postId) {
        return null;
    }

    @Override
    public IPost getRecentPost() {
        return recentPost;
    }

    @Override
    public IPost getCachedPost(String postId) {
        return null;
    }

    @Override
    public IPost get(int position) {
        return posts.get(position);
    }

    @Override
    public boolean isSaved(String postId) {
        return false;
    }

    @Override
    public void setRecentPost(IPost mPost) {
        recentPost = mPost;
    }

    @Override
    public boolean isRecentPost(String postId) {
        return recentPost != null && recentPost.getPostId().toString().equals(postId);
    }

    @Override
    public void clearPosts() {
        posts.clear();
    }

    @Override
    public int count() {
        return posts.size();
    }

    @Override
    public int getLastPostId() {
        return posts.size() > 0 ? posts.get(posts.size() - 1).getPostId() : 0;
    }

    public static IPostsProvider newInstance() {
        return new MuoCorePostsProvider();
    }
}
