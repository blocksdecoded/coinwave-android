package com.makeuseof.muocore.backend.interfaces;

import android.content.Context;

import java.util.Date;
import java.util.Map;

/**
 * Created by Alisher Alikulov on 1/18/17.
 * for project makeuseof-android
 * with Android Studio
 */

public interface IPost {
    Integer getPostId();
    String getTitle();
    String getUrl();
    String getHtml();
    Date getPostDate();
    Integer getLikesCount();
    String getFeaturedImageUrl();
    String getImageUrlForThumb(Context context);
    boolean isUnread();
    void setLikesCount(int likesCount);
}
