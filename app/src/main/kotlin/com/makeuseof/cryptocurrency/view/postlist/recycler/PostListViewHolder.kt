package com.makeuseof.cryptocurrency.view.postlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostListViewHolder(
        view: View,
        private val mListener: PostVHCLickListener
): RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener {
            mListener.onClick(adapterPosition)
        }
    }

    fun onBind(){

    }

    interface PostVHCLickListener{
        fun onClick(position: Int)
    }
}