package com.makeuseof.muocore.models;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Alikulov on 2/15/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class MuoCachedPosts {
    private static final String CACHED_POSTS = "last_saved_posts";

    public static void clearCachedPosts() {
        MuoCoreContext.getInstance().getGlobalPrefs().getPreferences().edit().remove(CACHED_POSTS).apply();
    }

    public static void cacheLastPosts(List<IPost> posts) {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            writer.beginArray();
            int limit = posts.size() > 20 ? 20 : posts.size() > 10 ? 10 : posts.size();
            for (IPost ipost : posts.subList(0, limit)) {
                Post post = (Post) ipost;
                writer.beginObject();
                writer.name("id").value(post.id);
                writer.name("title").value(post.title);
                writer.name("likes_count").value(post.likesCount);
                writer.name("featured_img").value(post.getFeaturedImageUrl());
                writer.name("thumb_img").value(post.image.mobile);
                writer.name("html").value(post.html);
                writer.name("url").value(post.url);
                writer.name("date").value(post.getFormattedDate());
                writer.endObject();
            }
            writer.endArray();
            MuoCoreContext.getInstance().getGlobalPrefs().getPreferences().edit().putString(CACHED_POSTS, stringWriter.toString()).apply();
        } catch (IOException ignored) {
        }
    }

    public static List<IPost> getCachedPosts() {
        List<IPost> posts = new ArrayList<>();
        try {
            String jsonSavedPosts = MuoCoreContext.getInstance().getGlobalPrefs().getPreferences().getString(CACHED_POSTS, null);
            if (jsonSavedPosts != null) {
                JsonReader reader = new JsonReader(new StringReader(jsonSavedPosts));
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    Post post = new Post();
                    post.image = new Image();
                    while (reader.hasNext()) {
                        try {
                            String key = reader.nextName();

                            switch (key) {
                                case "id":
                                    post.id = reader.nextInt();
                                    break;
                                case "title":
                                    post.title = reader.nextString();
                                    break;
                                case "likes_count":
                                    post.likesCount = reader.nextInt();
                                    break;
                                case "featured_img":
                                    try {
                                        post.image.featured = reader.nextString();
                                    } catch (Exception ignored) {
                                    }
                                    break;
                                case "thumb_img":
                                    try {
                                        post.image.mobile = reader.nextString();
                                    } catch (Exception ignored) {
                                    }
                                    break;
                                case "html":
                                    post.html = reader.nextString();
                                    break;
                                case "url":
                                    post.url = reader.nextString();
                                    break;
                                case "date":
                                    post.setPostDate(reader.nextString());
                                    break;
                                default:
                                    reader.skipValue();
                            }
                        } catch (Exception e) {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    posts.add(post);
                }
                reader.endArray();
                reader.close();
            }
        } catch (IOException ignored) {
        }
        return posts;
    }
}
