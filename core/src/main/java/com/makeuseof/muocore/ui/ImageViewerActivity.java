package com.makeuseof.muocore.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.ui.adapters.ImagesAdapter;
import com.makeuseof.muocore.ui.listeners.DefaultGestureListener;
import com.makeuseof.muocore.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alisher Alikulov on 12/26/16.
 * for project MuoLibTest
 * with Android Studio
 */

public class ImageViewerActivity extends BrowserCoreActivity {
    public final static String KEY_IMG_URLS = "img_urls";
    public static final Pattern PATTERN_IMG = Pattern.compile("<img[^>]*>");
    public static final Pattern PATTERN_SRC = Pattern.compile("src\\s*=\\s*(\\\"|')(([^\\\"';]*))(\\\"|')");

    private int mCurrentPos;
    private List<String> mImgUrls = new ArrayList<String>();
    private ViewPager mWidgetViewPager;
    private String postId;
    private IPostsProvider postsProvider = MuoCoreContext.getInstance().getPostProvider();
    private String url;
    private ImagesAdapter mAdapter;
    private GestureDetector gestureScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        initActionBar();


        mWidgetViewPager = (ViewPager) findViewById(R.id.view_pager);
        postId = getIntent().getStringExtra(CoreSharedConstants.KEY_POST_ID);
        url = getIntent().getStringExtra(CoreSharedConstants.KEY_POST_IMG_URL);

        if (postId == null && url == null) {
            finish();
            return;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        prepareData();

        gestureScanner = new GestureDetector(this, gestureListener);
        showData();
    }

    protected void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.image_viewer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_o);
        try {
            toolbar.setElevation(0);
        } catch (NoSuchMethodError ignored) {
        }
    }

    DefaultGestureListener gestureListener = new DefaultGestureListener() {
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
            if (Math.abs(velocityX) < Math.abs(velocityY)) {
                onBackPressed();
                if (velocityY > 0) {
                    overridePendingTransition(0, R.anim.slide_out_down);
                } else {
                    overridePendingTransition(0, R.anim.slide_out_up);
                }
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareData() {
        IPost post = postsProvider.findByID(postId);

        if (postsProvider.isRecentPost(postId)) {
            post = postsProvider.getRecentPost();
        } else if (post == null) {
            post = postsProvider.getPostFromDB(postId);
        }

        if (post != null) {

            Matcher imgMatcher = PATTERN_IMG.matcher(post.getHtml());
            while (imgMatcher.find()) {
                String imageHtmlTag = imgMatcher.group();
                if (!imageHtmlTag.contains("data-image=\"true\"") && !imageHtmlTag.contains("class=\"article__image\"")) {
                    continue;
                }

                Matcher srcMatcher = PATTERN_SRC.matcher(imageHtmlTag);
                srcMatcher.find();

                if (srcMatcher.groupCount() > 2) {
                    String imgUrl = srcMatcher.group(2);
                    if (imgUrl.startsWith("http://") || imgUrl.startsWith("https://") || imgUrl.startsWith("file://"))
                        mImgUrls.add(imgUrl);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        mAdapter.onDestroy();
        super.onDestroy();
    }


    public void shareImage() {
        if (mImgUrls.size() > 0) {
            String uri = mImgUrls.get(mWidgetViewPager.getCurrentItem());

            String filename = String.format("%08x", uri.hashCode());

            File photoFile = new File(getCacheDir(), filename);

            if (uri.startsWith("file://")) {
                photoFile = new File(uri.replace("file://", ""));
            }

            final File photoFileForShare = photoFile;

            if (!photoFileForShare.exists()) {
                // photo is loading from network
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File shareImage = new File(getFilesDir(), "share/image.png");
                        shareImage.delete();
                        shareImage.getParentFile().mkdirs();
                        shareImage.getParentFile().setExecutable(true, false);
                        shareImage.getParentFile().setWritable(true, false);
                        shareImage.getParentFile().setReadable(true, false);

                        FileUtils.copyFile(photoFileForShare, shareImage);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setType("image/png");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareImage));
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_media)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void showData() {
        mCurrentPos = mImgUrls.indexOf(url);

        if (mCurrentPos == -1) {
            mCurrentPos = 0;
        }

        mAdapter = new ImagesAdapter(this, mImgUrls, mCurrentPos, gestureScanner);
        mWidgetViewPager.setAdapter(mAdapter);
        mWidgetViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mCurrentPos != -1) {
            mWidgetViewPager.setCurrentItem(mCurrentPos);
        }
        updateTitle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!getSupportActionBar().isShowing())
                getSupportActionBar().show();

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void updateTitle() {
        getSupportActionBar().setTitle(String.format("%s of %s", mWidgetViewPager.getCurrentItem() + 1, mImgUrls.size()));
    }

}
