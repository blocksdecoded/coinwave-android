package com.makeuseof.muocore.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.facebook.FacebookSdk;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.models.IPostsProviderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Alikulov on 2/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class PostPagerActivity extends SwipeableActivity implements IPostsProviderListener {
    public final static String POST_POSITION = "post_position_key";
    public static final String SECONDARY_POST_PROVIDER_FLAG = "secondary_post_provider_flag";
    private ViewPager postsPager;
    private PagerAdapter pagerAdapter;
    private IPostsProvider postsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_pages);
        postsPager = (ViewPager) findViewById(R.id.postsPager);
        int position = getIntent().getIntExtra(POST_POSITION, 0);
        boolean useSecondaryPostProvider = getIntent().getBooleanExtra(SECONDARY_POST_PROVIDER_FLAG, false);
        if(useSecondaryPostProvider) {
            postsProvider = MuoCoreContext.getInstance().getSecondaryPostProvider();
        }else{
            postsProvider = MuoCoreContext.getInstance().getPostProvider();
        }
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        postsProvider.addListener(this);
        updatePosts();

        postsPager.setAdapter(pagerAdapter);
        postsPager.setOffscreenPageLimit(0);
        postsPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        postsProvider.removeListener(this);
    }

    @Override
    public void firstPostsLoaded(int count) {
        updatePosts();
    }

    private void updatePosts() {
        List<IPost> posts = new ArrayList<>();
        for (int i = 0; i < postsProvider.count(); i++) {
            posts.add(postsProvider.get(i));
        }
        pagerAdapter.setPosts(posts);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void newerPostsLoaded(int count, boolean isError, String errorMessage) {
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void olderPostsLoaded(int count, boolean isError, String errorMessage, boolean firstLoad) {
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewPostsAvailable(int count) {
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadingPostsFinish(boolean firstLoad, boolean older) {
        pagerAdapter.notifyDataSetChanged();
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private List<IPost> posts = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            PostViewFragment postViewFragment = new PostViewFragment();
            postViewFragment.setPostsProvider(postsProvider);
            postViewFragment.setPostId(String.valueOf(posts.get(position).getPostId()));
            return postViewFragment;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        public void setPosts(List<IPost> posts) {
            this.posts = posts;
        }
    }
}
