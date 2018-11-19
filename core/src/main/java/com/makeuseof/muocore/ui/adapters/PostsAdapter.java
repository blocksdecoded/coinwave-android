/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.models.IPostsProviderListener;
import com.makeuseof.muocore.ui.delegates.GrouviChatsDelegate;
import com.makeuseof.muocore.ui.holders.ListFooterViewHolder;
import com.makeuseof.muocore.ui.holders.PostFeaturedItemViewHolder;
import com.makeuseof.muocore.ui.holders.PostItemViewHolder;
import com.makeuseof.muocore.ui.listeners.ListItemsListener;
import com.makeuseof.muocore.ui.widgets.RadialMenuView;

import java.util.List;

/**
 * Posts Adapter
 */

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListFooterViewHolder.ClickListener, IPostsProviderListener {

    private static final int TYPE_ITEM_LEFT = 0;
    private static final int TYPE_ITEM_RIGHT = 3;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_FEATURED = 2;
    private final IPostsProvider mPostsProvider;
    private Context mContext;
    private List<Integer> mCategoryIds;
    private Integer mLastPostId;
    private boolean mAlreadyLoading;
    private boolean mErrorLoading;
    private boolean mAllLoaded;
    private ListItemsListener mListener;
    private PostItemViewHolder.ClickListener mOnItemClickListener;
    private ListFooterViewHolder mFooterHolder;
    private RadialMenuView radialMenu;
    private GrouviChatsDelegate grouviChatsDelegate = new GrouviChatsDelegate();
    private boolean isBackground = false;

    public PostsAdapter(Context context, List<Integer> categoryIds, IPostsProvider postsProvider, PostItemViewHolder.ClickListener onItemClickListener, ListItemsListener listItemsListener) {
        mPostsProvider = postsProvider;
        mPostsProvider.addListener(this);
        mContext = context;
        mCategoryIds = categoryIds;
        mOnItemClickListener = onItemClickListener;
        mListener = listItemsListener;
    }

    public void updateCategories(List<Integer> categoryIds) {
        mLastPostId = null;
        mAlreadyLoading = false;
        mErrorLoading = false;
        mAllLoaded = false;
        mCategoryIds = categoryIds;

        notifyDataSetChanged();
        loadNext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FEATURED) {
            return new PostFeaturedItemViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_featured_item, parent, false), mOnItemClickListener, radialMenu);
        } else if (viewType == TYPE_ITEM_LEFT) {
            return new PostItemViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_item, parent, false), mOnItemClickListener, true, radialMenu);
        } else if (viewType == TYPE_ITEM_RIGHT) {
            return new PostItemViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_item, parent, false), mOnItemClickListener, false, radialMenu);
        } else if (viewType == TYPE_FOOTER) {

            return new ListFooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_footer, parent, false), this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == getItemCount() - 3 && !mAlreadyLoading && !mAllLoaded) {
            loadNext();
        }

        if(getItemViewType(position) == TYPE_FEATURED) {
            IPost post = mPostsProvider.get(position);

            PostFeaturedItemViewHolder itemHolder = (PostFeaturedItemViewHolder) holder;
            itemHolder.bind(mContext, post);

        } else if (getItemViewType(position) == TYPE_ITEM_RIGHT || getItemViewType(position) == TYPE_ITEM_LEFT) {
            IPost post = mPostsProvider.get(position);
            PostItemViewHolder itemHolder = (PostItemViewHolder) holder;

            itemHolder.bind(mContext, post);
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            mFooterHolder = (ListFooterViewHolder) holder;

            if(mAlreadyLoading && mPostsProvider.count() > 0 ) {
                mFooterHolder.errorView.setVisibility(View.GONE);
                mFooterHolder.progressView.setVisibility(View.VISIBLE);
            } else {
                if(mErrorLoading) {
                    mFooterHolder.errorView.setVisibility(View.VISIBLE);
                }else{
                    mFooterHolder.errorView.setVisibility(View.GONE);
                }
                mFooterHolder.progressView.setVisibility(View.GONE);
            }
        }
    }

    public IPost getItem(int position) {
        if(mPostsProvider.count() <= position || position < 0){
            return null;
        }
        return mPostsProvider.get(position);
    }

    @Override
    public int getItemCount() {
        return mPostsProvider.count() + (mAllLoaded ? 0 : 1);
    }

    public boolean isFooter(int position){
        return position == mPostsProvider.count();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mPostsProvider.count()) {
            return TYPE_FOOTER;
        } else if (position % 5 == 0) {
            return TYPE_FEATURED;
        } else if ((position % 5) % 2 == 1){
            return TYPE_ITEM_LEFT;
        } else{
            return TYPE_ITEM_RIGHT;
        }
    }

    @Override
    public void firstPostsLoaded(int count) {
        if (count > 0) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.MAIN_MENU_UPDATE));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.UNREAD_COUNT_UPDATED_MESSAGE));
            notifyDataSetChanged();
        }
    }

    @Override
    public void newerPostsLoaded(int count, boolean isError, String errorMessage) {
        if(isError){
            mListener.onNewerLoadFailed(isBackground);
        }else{
            if (count > 0) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.MAIN_MENU_UPDATE));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.UNREAD_COUNT_UPDATED_MESSAGE));
                notifyItemRangeInserted(0, count);
            }
        }
    }

    @Override
    public void olderPostsLoaded(int count, boolean isError, String errorMessage, boolean firstLoad) {
        if(isError){
            if (firstLoad) {
                if(mPostsProvider.count() > 0){
                    notifyDataSetChanged();
                }else {
                    mListener.onFirstLoadNoConnection();
                }
            } else {
                if (mFooterHolder != null) {
                    mErrorLoading = true;
                    mFooterHolder.progressView.setVisibility(View.GONE);
                    mFooterHolder.errorView.setVisibility(View.VISIBLE);
                }
            }
        }else {
            if (count == 0) {
                if (firstLoad) {
                    mListener.onNoItems();
                } else {
                    mAllLoaded = true;
                    notifyDataSetChanged();
                }
            } else {
                if (mLastPostId == null) {
                    if (mPostsProvider.count() > 0) {
                        MuoCoreContext.getInstance().getGlobalPrefs().setPostLastId(mPostsProvider.getLastPostId()).commit();
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNewPostsAvailable(int count) {
        if (count > 0){
            mListener.onNewPostsAvailable();
        }
    }

    @Override
    public void onLoadingPostsFinish(boolean firstLoad, boolean newerPosts) {
        if (firstLoad) {
            mListener.onFirstLoadFinish();
        } else {
            if (mFooterHolder != null)
                mFooterHolder.progressView.setVisibility(View.GONE);
        }
        mAlreadyLoading = false;
        if(newerPosts) {
            mListener.onNewerLoadFinish(isBackground);
        }
    }

    public void loadNext() {
        mErrorLoading = false;
        mAlreadyLoading = true;

        mLastPostId =  mPostsProvider.getLastPostId();
        boolean installedGrouvi = grouviChatsDelegate.isGrouviInstalled(mContext);

        if (isFirstLoad()) {
            mListener.onFirstLoadStart();
        } else {
            if (mFooterHolder != null) {
                mErrorLoading = true;
                mFooterHolder.errorView.setVisibility(View.GONE);
                mFooterHolder.progressView.setVisibility(View.VISIBLE);
            }
        }

        mPostsProvider.loadOlder(mContext, mLastPostId, mCategoryIds, isFirstLoad(), installedGrouvi);
    }

    public void loadNewer(boolean isBackground) {
        mErrorLoading = false;
        this.isBackground = isBackground;
        mPostsProvider.loadNewer(mContext, mCategoryIds);
    }

    private boolean isFirstLoad() {
        return mPostsProvider.count() == 0;
    }

    @Override
    public void onFooterClick(View v, int position) {
        loadNext();
    }

    public void setRadialMenu(RadialMenuView radialMenu) {
        this.radialMenu = radialMenu;
    }

    @SuppressWarnings("unused")
    public void setGrouviChatsDelegate(GrouviChatsDelegate grouviChatsDelegate) {
        this.grouviChatsDelegate = grouviChatsDelegate;
    }
}
