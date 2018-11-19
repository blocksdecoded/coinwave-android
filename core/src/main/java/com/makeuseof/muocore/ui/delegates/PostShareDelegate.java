package com.makeuseof.muocore.ui.delegates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.models.PublisherPost;

import java.util.List;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class PostShareDelegate {
    public void sharePost(Activity activity, IPost mPost, int requestCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, mPost.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, mPost.getUrl());
        sendIntent.setType("text/plain");
        activity.startActivityForResult(Intent.createChooser(sendIntent, activity.getString(R.string.share)), requestCode);
    }

    public void sharePost(Activity activity, PublisherPost post, int requestCode){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, mPost.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, post.url);
        sendIntent.setType("text/plain");
        activity.startActivityForResult(Intent.createChooser(sendIntent, activity.getString(R.string.share)), requestCode);
    }

    public void onShareDone(Context context) {
        Toast.makeText(context, "Thanks for sharing", Toast.LENGTH_SHORT).show();
    }

    public void shareFacebookMessanger(Activity activity, IPost post, int requestCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n%s", post.getTitle(), post.getUrl()));
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        try {
            activity.startActivityForResult(sendIntent, requestCode);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "Please Install Facebook Messenger", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareFacebook(Activity activity, IPost post, int shareActionCode) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle(post.getTitle())
                .setContentUrl(Uri.parse(post.getUrl()))
                .build();
        ShareDialog shareDialog = new ShareDialog(activity);
        try {
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        } catch (Exception e) {
            Toast.makeText(activity, "Cannot share to Facebook", Toast.LENGTH_LONG).show();
        }
    }

    public void shareWhatsApp(Activity activity, IPost post, int requestCode) {
        Intent sendIntent = new Intent();
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n%s", post.getTitle(), post.getUrl()));
        sendIntent.setType("text/plain");
        try {
            activity.startActivityForResult(sendIntent, requestCode);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "Please Install Whatsapp", Toast.LENGTH_LONG).show();
        }
    }

    public void shareTwitter(Activity activity, IPost post, int requestCode) throws Exception {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s\n%s", post.getTitle(), post.getUrl()));
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        intent.setType("text/plain");

        final PackageManager packManager = activity.getPackageManager();
        final List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo: resolvedInfoList){
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                intent.setClassName(resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                activity.startActivityForResult(intent, requestCode);
                return;
            }
        }

        throw new Exception("Cannot share to twitter!");
    }
}
