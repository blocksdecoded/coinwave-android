/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StyleableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.ui.listeners.CoreBaseActivityListener;

/**
 * Core Base Activity
 */

public abstract class CoreBaseActivity extends AppCompatActivity {
    private CoreBaseActivityListener coreBaseActivityListener;
    private boolean statusBarHidden = false;

    private BroadcastReceiver mSystemCheckTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.please_check_system_time), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                }
            }).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coreBaseActivityListener = MuoCoreContext.getInstance().getCoreBaseActivityListener();
        coreBaseActivityListener.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        coreBaseActivityListener.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        coreBaseActivityListener.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void subscribeEvents() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mSystemCheckTime, new IntentFilter(CoreSharedConstants.Broadcast.SYSTEM_TIME_ERROR));
    }

    protected void unsubscribeEvents() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSystemCheckTime);
    }

    public static int getAttr(int attrId, Resources.Theme theme){
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static Drawable getStyleableDrawable(@StyleableRes int[] attrs, int attrId, Resources.Theme theme){
        TypedArray a = theme.obtainStyledAttributes(0, attrs);
        return a.getDrawable(attrId);
    }

    public void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        statusBarHidden = true;
    }

    public boolean isStatusBarHidden(){
        return statusBarHidden;
    }

    public void showStatusBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        statusBarHidden = false;
    }

    public void openLink(String url){
        if(url == null){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
