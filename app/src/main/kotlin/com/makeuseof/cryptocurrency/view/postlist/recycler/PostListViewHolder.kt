package com.makeuseof.cryptocurrency.view.postlist.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
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
): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    @BindView(R.id.adapter_post_item_image)
    lateinit var mImage: ImageView
    @BindView(R.id.adapter_post_item_title)
    lateinit var mTitle: TextView

    init {
        ButterKnife.bind(this, view)
        itemView.setOnClickListener {
            mListener.onClick(adapterPosition)
        }
    }

    fun onBind(post: PublisherPost){
        mTitle.text = post.title

        post.image?.featured?.also {
            Glide.with(mImage.context)
                    .load(it)
                    .thumbnail(0.01f)
                    .into(mImage)
//            mImage.loadImageFromUrl(it)
        }
    }

    interface PostVHCLickListener{
        fun onClick(position: Int)
    }
}