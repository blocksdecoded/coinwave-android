package com.makeuseof.muocore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.models.Image;
import com.makeuseof.muocore.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Alikulov on 3/8/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class MuoCoreDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "muo_core_posts";
    private static final String POSTS_TABLE_NAME = "muo_core_posts";

    public MuoCoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPostsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE_NAME);
//        onCreate(db);
    }

    private void createPostsTable(SQLiteDatabase db) {
        String query = new SQLCreateTableMaker(POSTS_TABLE_NAME)
                .addPrimaryKey("post_id", SQLCol.Type.Int)
                .addText("date")
                .addText("title")
                .addInt("likes_count")
                .addText("featured_image_url")
                .addText("middle_image_url")
                .addText("thumb_image_url")
                .addText("original_image_url")
                .addText("html")
                .addText("url")
                .make();
        db.execSQL(query);
    }

    public boolean savePost(Post post) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("post_id", post.id);
            values.put("title", post.title);
            values.put("date", post.getFormattedDate());
            values.put("likes_count", post.likesCount);
            values.put("featured_image_url", post.image.featured);
            values.put("middle_image_url", post.image.middle);
            values.put("thumb_image_url", post.image.thumb);
            values.put("original_image_url", post.image.post);
            values.put("html", post.html);
            values.put("url", post.url);

            db.insert(POSTS_TABLE_NAME, null, values);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePost(Post post) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("title", post.title);
            values.put("date", post.getFormattedDate());
            values.put("likes_count", post.likesCount);
            values.put("featured_image_url", post.image.featured);
            values.put("middle_image_url", post.image.middle);
            values.put("thumb_image_url", post.image.thumb);
            values.put("original_image_url", post.image.post);
            values.put("html", post.html);
            values.put("url", post.url);

            db.update(POSTS_TABLE_NAME, values, "post_id = ?", new String[]{String.valueOf(post.getPostId())});
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Post> readAllPosts() {
        List<Post> posts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + POSTS_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Post post = readPostFromCursor(cursor);
                posts.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return posts;
    }

    public boolean isSaved(String postId) {
        String countQuery = "SELECT  * FROM " + POSTS_TABLE_NAME + " WHERE post_id = '" + postId+ "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    public boolean deletePost(Post post) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(POSTS_TABLE_NAME, "post_id" + " = ?", new String[]{String.valueOf(post.id)});
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String notNull(String text) {
        if (text == null) {
            return "";
        }
        return text;
    }

    public IPost getPost(String postId) {
        String selectQuery = "SELECT  * FROM " + POSTS_TABLE_NAME + " WHERE post_id = " + postId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Post post = null;
        if (cursor.moveToFirst()) {
            post = readPostFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return post;
    }

    @NonNull
    private Post readPostFromCursor(Cursor cursor) {
        Post post = new Post();
        post.id = cursor.getInt(cursor.getColumnIndex("post_id"));
        post.title = cursor.getString(cursor.getColumnIndex("title"));
        post.setPostDate(cursor.getString(cursor.getColumnIndex("date")));
        post.likesCount = cursor.getInt(cursor.getColumnIndex("likes_count"));
        Image img = new Image();
        img.featured = cursor.getString(cursor.getColumnIndex("featured_image_url"));
        img.middle = cursor.getString(cursor.getColumnIndex("middle_image_url"));
        img.thumb = cursor.getString(cursor.getColumnIndex("thumb_image_url"));
        img.post = cursor.getString(cursor.getColumnIndex("original_image_url"));
        post.image = img;
        post.html = cursor.getString(cursor.getColumnIndex("html"));
        post.url = cursor.getString(cursor.getColumnIndex("url"));
        return post;
    }
}
