/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.ui.adapters.SavesAdapter;
import com.makeuseof.muocore.ui.listeners.ClickListener;

/**
 * Saves Fragment
 */
public class SavesFragment extends Fragment implements ClickListener {

    private SavesAdapter mAdapter;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.updatePosts();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(CoreSharedConstants.SAVED_POST_UPDATED_MESSAGE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_posts, container, false);

        RecyclerView posts = (RecyclerView) rootView.findViewById(R.id.fragment_posts_list_posts);
        TextView emptyText = (TextView) rootView.findViewById(R.id.fragment_posts_list_empty);
        emptyText.setText(R.string.saves_list_empty);

        mAdapter = new SavesAdapter(getActivity(), posts, emptyText);
        posts.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        posts.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onClick(View v, int position) {
        startActivity(new Intent(getActivity(), PostViewActivity.class).putExtra(CoreSharedConstants.KEY_POST_ID, String.valueOf(mAdapter.getItem(position).id)));
        getActivity().overridePendingTransition(R.anim.slide_in_right , 0);
    }

    @Override
    public void onLongClick(View v, int position) {
    }

}
