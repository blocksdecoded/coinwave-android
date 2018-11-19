package com.makeuseof.muocore.ui.delegates;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class GrouviChatsDelegate {

    public void openChat(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("im.grouvi.app");
        context.startActivity(intent);
    }

    public boolean isGrouviInstalled(Context context) {
        try{
            context.getPackageManager().getPackageInfo("im.grouvi.app", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean isGrouviChatUrl(String url) {
        return url.startsWith("https://grou.vi/");
    }
}
