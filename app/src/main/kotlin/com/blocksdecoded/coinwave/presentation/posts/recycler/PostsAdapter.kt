package com.blocksdecoded.coinwave.presentation.posts.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.core.contracts.LoadNextListener
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.presentation.widgets.FooterViewHolder
import com.blocksdecoded.utils.extensions.height
import com.blocksdecoded.utils.extensions.inflate
import com.blocksdecoded.utils.extensions.isValidIndex

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostsAdapter(
    private var mItems: ArrayList<PublisherPost>,
    private val mListener: PostViewHolder.PostVHCLickListener,
    private val mLoadNextListener: LoadNextListener,
    private val mPostHeight: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FooterViewHolder.ClickListener {

    companion object {
        private val POST = 1
        private val FOOTER = 2
    }

    private var mAlreadyLoading = false
    private val mAllLoaded = false
    private var mFooterView: FooterViewHolder? = null

    //region Override

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder = when (p1) {
        1 -> PostViewHolder(p0.inflate(R.layout.item_post)!!, mListener).apply {
            this.height = mPostHeight
        }
        else -> FooterViewHolder(
            p0.inflate(R.layout.item_footer)!!,
            this
        )
    }

    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (mItems.isValidIndex(p1)) {
            when (p0) {
                is PostViewHolder -> p0.onBind(mItems[p1])
                is FooterViewHolder -> {
                    mFooterView = p0
                    if (mAlreadyLoading) {
                        mFooterView?.errorView?.visibility = View.GONE
                        mFooterView?.progressView?.visibility = View.VISIBLE
                    } else {
                        mFooterView?.errorView?.visibility = View.GONE
                        mFooterView?.progressView?.visibility = View.GONE
                    }
                }
            }
        }
        if (p1 == itemCount - 4 && !mAlreadyLoading && !mAllLoaded) {
            loadNext()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == mItems.size) {
            return FOOTER
        }
        return POST
    }

    //endregion

    private fun loadNext() {
        mAlreadyLoading = true

        mFooterView?.errorView?.visibility = View.GONE
        mFooterView?.progressView?.visibility = View.VISIBLE

        mLoadNextListener.onLoadNext()
    }

    override fun onFooterClick(v: View, position: Int) {
    }

    //region Public

    fun setItems(posts: List<PublisherPost>) {
        mAlreadyLoading = false
        mItems.clear()
        mItems.addAll(posts)
        notifyDataSetChanged()
    }

    fun addItems(posts: List<PublisherPost>) {
        mAlreadyLoading = false
        mItems.addAll(posts)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): PublisherPost? = if (mItems.isValidIndex(position)) {
        mItems[position]
    } else {
        null
    }

    //endregion
}