/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Post View Activity
 */
public class PostViewActivity extends SwipeableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideStatusBar();
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_post_view);
        onNewIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int defaultPlaceholder = MuoCoreContext.getInstance().getDefaultPlaceholder();
            ImageView image = (ImageView) findViewById(R.id.post_img);
            String imgUrlForThumb = getIntent().getStringExtra("IPOST");

            if(imgUrlForThumb != null && imgUrlForThumb != "") {

                try {
                    Picasso.with(this)
                            .load(imgUrlForThumb)
                            .error(defaultPlaceholder)
                            .placeholder(defaultPlaceholder)
                            .into(image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    onImageReady();
                                }

                                @Override
                                public void onError() {
                                }
                            });
                } catch (Exception ignored) {
                    image.setImageResource(defaultPlaceholder);
                }
            }
            onImageReady();
        } else {
            onImageReady();
        }


    }

    void onImageReady(){

        String postId = getIntent().getStringExtra(CoreSharedConstants.KEY_POST_ID);

        if (postId != null) {
            IPostsProvider postsProvider = MuoCoreContext.getInstance().getPostProvider();
            PostViewFragment postViewFragment = new PostViewFragment();
            postViewFragment.setPostsProvider(postsProvider);
            postViewFragment.setPostId(postId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_post_view_fragment, postViewFragment);
            ft.commitAllowingStateLoss();
        }else{
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        overridePendingTransition(0, R.anim.slide_out_right);
    }
}