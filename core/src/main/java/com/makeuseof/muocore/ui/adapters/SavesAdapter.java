/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.Post;
import com.makeuseof.muocore.ui.delegates.BookmarkDelegate;
import com.makeuseof.muocore.ui.holders.SavedPostViewHolder;
import com.makeuseof.muocore.ui.listeners.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves Adapter
 */
public class SavesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BookmarkDelegate bookmarkDelegate = MuoCoreContext.getInstance().getBookmarkDelegate();

    public static String TAG = SavesAdapter.class.getName();

    private Context mContext;
    private List<Post> mPosts;
    private View mEmptyText;
    private View mList;

    private ClickListener mClickListener;

    public SavesAdapter(Context context, View list, View emptyText) {
        mContext = context;
        mPosts = new ArrayList<>();
        mList = list;
        mEmptyText = emptyText;
        updatePosts();
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void updatePosts() {
        mEmptyText.setVisibility(View.GONE);

        new AsyncTask<Void, Void, List<Post>>() {
            protected List<Post> doInBackground(Void... unused) {
                return Post.backvertList(bookmarkDelegate.getBookMarkedPosts());
            }
            protected void onPostExecute(List<Post> posts) {
                mPosts = posts;

                notifyDataSetChanged();

                if (mPosts.size() == 0) {
                    mEmptyText.setVisibility(View.VISIBLE);
                    mList.setVisibility(View.GONE);
                } else {
                    mList.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    public Post getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedPostViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_saved_post, parent, false), mClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = getItem(position);
        SavedPostViewHolder itemHolder = (SavedPostViewHolder) holder;

        itemHolder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

}
