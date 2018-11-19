package com.makeuseof.muocore.ui.delegates;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.db.MuoCoreDBHelper;
import com.makeuseof.muocore.models.Post;
import com.makeuseof.muocore.ui.listeners.ProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class BookmarkDelegate {
    private static final Pattern PATTERN_IMG = Pattern.compile("<img[^>]*>");
    private static final Pattern PATTERN_SRC = Pattern.compile("src\\s*=\\s*(\\\"|')(([^\\\"';]*))(\\\"|')");
    private static final String FOLDER_NAME = "posts_images";

    private MuoCoreDBHelper dbHelper;

    public boolean bookmarkingEnabled() {
        return dbHelper != null;
    }

    public void bookmarkClicked(Context context, IPost mPost) {
        if(dbHelper != null){
            if(isSaved(mPost)){
                dbHelper.deletePost((Post) mPost);
                clearCache(context);
                Toast.makeText(context, context.getString(R.string.post_deleted_from_saves), Toast.LENGTH_SHORT).show();
            }else {
                dbHelper.savePost((Post) mPost);
                saveImages(context, (Post)mPost, null);
                Toast.makeText(context, context.getString(R.string.post_saved), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(CoreSharedConstants.SAVED_POST_UPDATED_MESSAGE);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    public boolean isBookmarked(IPost post) {
        return dbHelper!=null && post!=null && dbHelper.isSaved(post.getPostId().toString());
    }

    public boolean isBookmarked(String idOrSlug) {
        return dbHelper!=null && dbHelper.isSaved(idOrSlug);
    }

    public boolean isSaved(IPost post) {
        return dbHelper!=null && post != null && dbHelper.isSaved(post.getPostId().toString());
    }

    public void setDbHelper(MuoCoreDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<IPost> getBookMarkedPosts() {
        if(dbHelper != null){
            return Post.convertList(dbHelper.readAllPosts());
        }
        return new ArrayList<>();
    }

    public IPost getPostById(String postId) {
        return dbHelper.getPost(postId);
    }

    private void saveImages(final Context context, final Post post, final ProgressListener onAction) {
        if(post == null || post.html == null){
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                clearCache(context);

                Matcher imgMatcher = PATTERN_IMG.matcher(post.html);
                final String postPath = context.getFilesDir().getPath() + "/" + FOLDER_NAME + "/" + post.id + "/";
                final List<String> imagesList = new ArrayList<>();
                final List<String> completedList = new ArrayList<>();
                final List<String> failedList = new ArrayList<>();

                while (imgMatcher.find()) {
                    Matcher srcMatcher = PATTERN_SRC.matcher(imgMatcher.group());
                    try {
                        srcMatcher.find();
                        if (srcMatcher.groupCount() > 2) {
                            String imgUrl = srcMatcher.group(2);

                            if (imgUrl.startsWith("http://") || imgUrl.startsWith("https://")) {
                                imagesList.add(imgUrl);
                            }
                        }
                    }catch (Exception ignore){}
                }

                int i = 0;

                for (final String imgUrl : imagesList) {
                    final int fileId = i++;
                    File newFilePath = new File(postPath + fileId);
                    newFilePath.getParentFile().mkdirs();

                    try {
                        downloadFile(imgUrl, newFilePath.getPath());

                        if (newFilePath.exists() && newFilePath.isFile()) {
                            post.html = post.html.replace(imgUrl, "file://" + newFilePath.getPath());
                        }
                        completedList.add(imgUrl);
                    } catch (Exception e) {
                        failedList.add(imgUrl);
                    }

                    if (imagesList.size() == completedList.size()) {
                        if (isSaved(post)) {
                            dbHelper.updatePost(post);
                        }

                        if(onAction != null)
                            onAction.onCompleted();
                    } else if (imagesList.size() == completedList.size() + failedList.size()) {

                        if (isSaved(post)) {
                            dbHelper.updatePost(post);
                        }

                        if(onAction != null)
                            onAction.onFail();
                    }
                }
            }
        }).start();
    }

    private void clearCache(Context context){
        File f = new File(context.getFilesDir().getPath() + "/" + FOLDER_NAME );
        File[] files = f.listFiles();

        if(files == null) {
            return;
        }

        for (File fileOrDir : files) {
            if (fileOrDir.isDirectory()) {
                try {
                    int postId = Integer.parseInt(fileOrDir.getName());

                    if(!isBookmarked(String.valueOf(postId))) {
                        deleteRecursive(fileOrDir);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        try {
            fileOrDirectory.delete();
        }catch (Exception ignored){}
    }

    private static void downloadFile(String downloadUrl, String outputPath) throws Exception {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception();
            }

            input = connection.getInputStream();
            output = new FileOutputStream(outputPath);

            byte data[] = new byte[4096];
            int count;

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

        } finally {
            try {
                if (output != null)
                    output.close();
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                if (input != null)
                    input.close();
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                if (connection != null)
                    connection.disconnect();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
