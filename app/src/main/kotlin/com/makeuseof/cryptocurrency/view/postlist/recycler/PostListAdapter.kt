package com.makeuseof.cryptocurrency.view.postlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.makeuseof.cryptocurrency.R
import com.makeuseof.muocore.models.PublisherPost
import com.makeuseof.utils.inflate
import com.makeuseof.utils.isValidIndex
import com.makeuseof.utils.setHeight

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostListAdapter(
        private var mItems: ArrayList<PublisherPost>,
        private val mListener: PostListViewHolder.PostVHCLickListener,
        private val mPostHeight: Int
) : RecyclerView.Adapter<PostListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostListViewHolder =
            PostListViewHolder(p0.inflate(R.layout.adapter_post_list_item)!!, mListener).apply {
                setHeight(mPostHeight)
            }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(p0: PostListViewHolder, p1: Int) {
        if (mItems.isValidIndex(p1)){
            p0.onBind(mItems[p1])
        }
    }

    fun setItems(posts: List<PublisherPost>) {
        mItems.clear()
        mItems.addAll(posts)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): PublisherPost? = if (mItems.isValidIndex(position)) {
        mItems[position]
    } else {
        null
    }
}