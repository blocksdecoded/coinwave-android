package com.makeuseof.muocore.util;

import com.makeuseof.muocore.GlobalPrefs;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alisher Alikulov on 11/15/16.
 * for project makeuseof-android
 * with Android Studio
 */

public class LikesUtil {
    private static LikesUtil instanse;
    private static final String POST_LIKES = "muo_liked_posts_list";
    private final GlobalPrefs prefs;
    private final Set<Integer> likes;

    public static LikesUtil getInstance() {
        if(instanse == null){
            instanse = new LikesUtil(MuoCoreContext.getInstance().getGlobalPrefs());
        }
        return instanse;
    }

    private LikesUtil(GlobalPrefs globalPrefs) {
        this.prefs = globalPrefs;
        this.likes = readLikes(prefs.getPreferences().getString(POST_LIKES, null));
    }

    private Set<Integer> readLikes(String string) {
        Set<Integer> posts = new HashSet<>();
        try {
            String[] ids = string.split(",");
            for(String postId: ids){
                posts.add(Integer.parseInt(postId));
            }
        }catch (Exception ignored){}
        return posts;
    }

    public void like(IPost post) {
        likes.add(post.getPostId());
        prefs.getPreferences().edit().putString(POST_LIKES, writeLikes()).apply();
    }

    private String writeLikes() {
        if (likes.size() > 0) {
            StringBuilder strBuilder = new StringBuilder();

            for (Integer postId : likes) {
                strBuilder.append(postId).append(",");
            }

            strBuilder.deleteCharAt(strBuilder.length() - 1);

            return strBuilder.toString();
        } else {
            return "";
        }
    }

    public boolean isLiked(IPost post) {
        return likes.contains(post.getPostId());
    }
}
