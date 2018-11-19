/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.models;

import android.content.Context;
import android.util.DisplayMetrics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.makeuseof.muocore.backend.interfaces.IPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Post implements IPost {
    @JsonProperty("ID")
    public int id;

    public Date postDate;

    @JsonProperty("post_title")
    public String title;

    @JsonProperty("likes_count")
    public int likesCount;

    @JsonProperty("featured_image")
    public Image image;

    public String html;

    public String url;

    public boolean isUnread;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public Post() {
        super();
    }

    @JsonSetter("post_date")
    public void setPostDate(final String postDate) {
        try {
            this.postDate = dateFormat.parse(postDate);
        } catch (Exception e) {
            e.printStackTrace();
            this.postDate = new Date();
        }
    }

    public String getFormattedDate(){
        if(postDate != null) {
            return dateFormat.format(postDate);
        }
        return "";
    }

    public String getImageUrl() {
        if(image == null) {
            return null;
        }
        if(image.thumb != null)
            return image.thumb;
        if(image.middle != null)
            return image.middle;
        return image.featured;
    }

    public String getImageUrlForThumb(Context context){
        if(image == null){
            return null;
        }

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        if(metrics.density >= 2 && image.mobile != null) { // for xxhdpi
            return image.mobile;
        } else if (image.featured != null){ // for xhdpi
            return image.featured;
        }

        return image.mobile;
    }

    @Override
    public boolean isUnread() {
        return isUnread;
    }

    @Override
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }


    @Override
    public Integer getPostId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getHtml() {
        return html;
    }

    @Override
    public Date getPostDate() {
        return postDate;
    }

    @Override
    public Integer getLikesCount() {
        return likesCount;
    }

    public String getFeaturedImageUrl(){
        if(image == null) {
            return null;
        }

        if(image.mobile != null){
            return image.mobile;
        }

        if(image.post == null){
            return image.featured;
        }

        return image.post;
    }

    public static List<IPost> convertList(List<Post> posts) {
        List<IPost> list = new ArrayList<>();
        list.addAll(posts);
        return list;
    }

    public static List<Post> backvertList(List<IPost> posts) {
        List<Post> list = new ArrayList<>();
        for(IPost post: posts){
            if(post instanceof Post){
                list.add((Post) post);
            }
        }
        return list;
    }
}
