package com.makeuseof.cryptocurrency.view.postlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.utils.loadImageFromUrl

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostListViewHolder(
        view: View,
        private val mListener: PostVHCLickListener
): RecyclerView.ViewHolder(view) {

    private val mImage: ImageView = itemView.findViewById(R.id.adapter_post_item_image)
    private val mTitle: TextView = itemView.findViewById(R.id.adapter_post_item_title)

    init {
        itemView.setOnClickListener {
            mListener.onClick(adapterPosition)
        }
    }

    fun onBind(post: PublisherPost){
        mTitle.text = post.title

        post.image?.featured?.also {
            mImage.loadImageFromUrl(it)
        }
    }

    interface PostVHCLickListener{
        fun onClick(position: Int)
    }
}