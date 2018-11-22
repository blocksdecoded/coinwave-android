package com.makeuseof.cryptocurrency.view.postlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
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

    @BindView(R.id.adapter_post_item_image)
    lateinit var mImage: ImageView
    @BindView(R.id.adapter_post_item_title)
    lateinit var mTitle: TextView
    @BindView(R.id.adapter_post_item_underline)
    lateinit var mUnderline: View

    init {
        ButterKnife.bind(this, view)

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