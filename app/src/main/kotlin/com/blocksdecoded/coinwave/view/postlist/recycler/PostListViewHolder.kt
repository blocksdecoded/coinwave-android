package com.blocksdecoded.coinwave.view.postlist.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.view.postlist.util.setRandomBg

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostListViewHolder(
    view: View,
    private val mListener: PostVHCLickListener
) : RecyclerView.ViewHolder(view) {
    @BindView(R.id.adapter_post_item_image)
    lateinit var mImage: ImageView
    @BindView(R.id.adapter_post_item_title)
    lateinit var mTitle: TextView

    @OnClick(
            R.id.adapter_post_item_read_more,
            R.id.adapter_post_item_selectable
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.adapter_post_item_read_more -> mListener.onClick(adapterPosition)
            R.id.adapter_post_item_selectable -> mListener.onClick(adapterPosition)
        }
    }

    init {
        ButterKnife.bind(this, view)
    }

    fun onBind(post: PublisherPost) {
        itemView.setRandomBg(adapterPosition)

        mTitle.text = post.title

        post.image?.featured?.also {
            Glide.with(mImage.context)
                    .load(it)
                    .thumbnail(0.01f)
                    .into(mImage)
//            mImage.loadImageFromUrl(it)
        }
    }

    interface PostVHCLickListener {
        fun onClick(position: Int)
    }
}