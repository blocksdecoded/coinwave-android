package com.makeuseof.cryptocurrency.view.postlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.domain.model.Post
import com.makeuseof.utils.inflate

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostListAdapter(
        private var mItems: ArrayList<Post>,
        private val mListener: PostListViewHolder.PostVHCLickListener
) : RecyclerView.Adapter<PostListViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostListViewHolder =
            PostListViewHolder(p0.inflate(R.layout.adapter_post_list_item)!!, mListener)

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(p0: PostListViewHolder, p1: Int) {
        p0.onBind()
    }
}