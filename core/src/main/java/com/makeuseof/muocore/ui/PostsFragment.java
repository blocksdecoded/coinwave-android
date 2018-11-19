/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.ui.adapters.PostsAdapter;
import com.makeuseof.muocore.ui.holders.PostItemViewHolder;
import com.makeuseof.muocore.ui.listeners.ListItemsListener;
import com.makeuseof.muocore.ui.widgets.LoadingView;
import com.makeuseof.muocore.ui.widgets.RadialMenuView;
import com.makeuseof.muocore.util.DeviceUtils;
import com.sloop.utils.fonts.FontsManager;

import java.util.ArrayList;
import java.util.List;

import tr.xip.errorview.ErrorView;

/**
 * Posts Fragment
 */

public class PostsFragment extends Fragment implements PostItemViewHolder.ClickListener, ListItemsListener {
    private static final int SHARE_ACTION = 1223;

    private List<Integer> mCategoryIds = new ArrayList<>();
    private IPostsProvider postsProvider;

    private RecyclerView postsRecyclerView;
    private PostsAdapter mAdapter;
    private ErrorView mErrorView;
    private LoadingView mLoadProgress;
    private TextView mEmptyListText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private boolean isTablet = false;

    private View mNewPosts;
    private float mNewPostsTranslationY;

    private BroadcastReceiver mSubscriptionsUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mCategoryIds = postsProvider.getSubscribedCategoriesIds();
                postsProvider.clearPosts();
                mAdapter.updateCategories(mCategoryIds);
                postsRecyclerView.scrollToPosition(0);
            }
        }
    };
    private BroadcastReceiver mNewPostsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.loadNewer(true);
            }
        }
    };
    private BroadcastReceiver mRefreshListOnUpdateUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    private RadialMenuView mRadialMenuView;

    public PostsFragment(){
        this.setPostsProvider(MuoCoreContext.getInstance().getPostProvider());
    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = DeviceUtils.isTablet(getActivity());
        mCategoryIds = postsProvider.getSubscribedCategoriesIds();
        subscribeEvents();
    }

    public void subscribeEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mNewPostsReceiver, new IntentFilter(CoreSharedConstants.Broadcast.NEW_POSTS_MESSAGE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSubscriptionsUpdatedReceiver, new IntentFilter(CoreSharedConstants.Broadcast.SUBSCRIPTIONS_UPDATED_MESSAGE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRefreshListOnUpdateUpdatedReceiver, new IntentFilter(CoreSharedConstants.Broadcast.UNREAD_COUNT_UPDATED_MESSAGE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRefreshListOnUpdateUpdatedReceiver, new IntentFilter(CoreSharedConstants.Broadcast.POST_LIKED));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRefreshListOnUpdateUpdatedReceiver, new IntentFilter(CoreSharedConstants.Broadcast.POST_OPENED_FIRST_TIME));
    }

    public void unsubscribeEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mSubscriptionsUpdatedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mNewPostsReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRefreshListOnUpdateUpdatedReceiver);
    }

    @Override
    public void onDestroy() {
        unsubscribeEvents();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (postsProvider.shouldFetchNewPosts()) {
                mAdapter.loadNewer(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);

        postsRecyclerView = (RecyclerView) rootView.findViewById(R.id.posts);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        if(mRadialMenuView != null) {
            mRadialMenuView.addParentView(postsRecyclerView);
        }

        mAdapter = new PostsAdapter(getActivity(), mCategoryIds, postsProvider, this, this);
        mAdapter.setRadialMenu(mRadialMenuView);
        postsRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(), isTablet?4:2){
            @Override
            public boolean canScrollVertically() {
                if(mRadialMenuView!=null) {
                    return !mRadialMenuView.isVisible();
                }else{
                    return super.canScrollVertically();
                }
            }
        };

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mAdapter!=null && mAdapter.isFooter(position)){
                    return isTablet?4:2;
                }
                if (position % 5 == 0) {
                    return isTablet?4:2;
                }
                return 1;
            }
        });
        postsRecyclerView.setLayoutManager(mLayoutManager);

        mEmptyListText = (TextView) rootView.findViewById(android.R.id.empty);
        mErrorView = (ErrorView) rootView.findViewById(R.id.fragment_post_error_view);
        mLoadProgress = (LoadingView) rootView.findViewById(R.id.fragment_post_progress);
        mNewPosts = rootView.findViewById(R.id.new_posts);
        mNewPostsTranslationY = mNewPosts.getTranslationY();

        mErrorView.setTitle(R.string.error_network);
        mErrorView.setSubtitle(R.string.error_network_subtitle);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                mAdapter.loadNext();
            }
        });
        mEmptyListText.setText(getActivity().getString(R.string.empty_post_list_text));

        FontsManager.initFormAssets(getActivity(), "fonts/Roboto-Light.ttf");
        FontsManager.changeFonts(mErrorView);

        // load only for latest first time and for selected category
        if (!postsProvider.hasPosts()) {
            mAdapter.loadNext();
        }else{
            postsProvider.checkNewPosts(getActivity());
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadNewer(false);
            }
        });

        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLayoutManager.findFirstVisibleItemPosition() == 0 && isNewPostsBadgeVisible()) {
                    hideNewPostsBadge();
                }
            }
        });

        mNewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutManager.smoothScrollToPosition(postsRecyclerView, null, 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHARE_ACTION) {
            Toast.makeText(getActivity(), "Thanks for sharing", Toast.LENGTH_LONG).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFirstLoadNoConnection() {
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFirstLoadStart() {
        mLoadProgress.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyListText.setVisibility(View.GONE);
    }

    @Override
    public void onFirstLoadFinish() {
        mLoadProgress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNoItems() {
        mEmptyListText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNewerLoadFinish(boolean isBackground) {
        if (isBackground) {
            showNewPostsBadge();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            mLayoutManager.smoothScrollToPosition(postsRecyclerView, null, 0);
        }
    }

    @Override
    public void onNewerLoadFailed(boolean isBackground) {
        if (!isBackground) {
            mSwipeRefreshLayout.setRefreshing(false);

            if (getView() != null)
                Snackbar.make(getView(), getString(R.string.no_internet_connection_or_server_unreachable), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNewPostsAvailable(){
        showNewPostsBadge();
    }

    @Override
    public void onClick(View v, int position) {
        mAdapter.notifyDataSetChanged();
        IPost post = mAdapter.getItem(position);

        if (post == null) {
            return;
        }


        Intent intent = new Intent(getActivity(), PostViewActivity.class);
        intent.putExtra(CoreSharedConstants.KEY_POST_ID, String.valueOf(post.getPostId()));


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            String imgUrlForThumb = post.getImageUrlForThumb(getActivity());
            View postImage = v.findViewById(R.id.post_item_img) == null ? v.findViewById(R.id.featured_post_item_img) : v.findViewById(R.id.post_item_img);
            intent.putExtra("IPOST", imgUrlForThumb);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), postImage, getString(R.string.image_transition));
            startActivity(intent, options.toBundle());
        } else{
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, 0);
        }
    }

    @Override
    public void onLongClick(View v, int position) {
        if(mRadialMenuView != null) {
            RecyclerView.ViewHolder clone = mAdapter.onCreateViewHolder(postsRecyclerView, mAdapter.getItemViewType(position));
            mAdapter.onBindViewHolder(clone, position);
            mRadialMenuView.setItemPosition(position);
            mRadialMenuView.showAroundView(v, clone.itemView);
        }
    }

    private void showNewPostsBadge() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mNewPosts, "translationY", mNewPostsTranslationY, 0);
        animator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNewPostsBadgeVisible()) {
                    hideNewPostsBadge();
                }
            }
        }, 5000);
    }

    private void hideNewPostsBadge() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mNewPosts, "translationY", 0, mNewPostsTranslationY);
        animator.start();
    }

    private boolean isNewPostsBadgeVisible() {
        return mNewPosts.getTranslationY() == 0;
    }

    public void setRadialMenuView(RadialMenuView mRadialMenuView) {
        this.mRadialMenuView = mRadialMenuView;
    }

    public PostsAdapter getAdapter() {
        return mAdapter;
    }

    public void setPostsProvider(IPostsProvider postsProvider) {
        this.postsProvider = postsProvider;
    }
}
