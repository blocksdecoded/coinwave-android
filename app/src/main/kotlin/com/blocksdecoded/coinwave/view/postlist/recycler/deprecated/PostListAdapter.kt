package com.blocksdecoded.coinwave.view.postlist.recycler.deprecated

import android.view.View
import android.view.ViewGroup
import com.blocksdecoded.core.contracts.LoadNextListener
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.view.postlist.recycler.ListFooterViewHolder
import com.blocksdecoded.coinwave.view.postlist.recycler.PostListViewHolder
import com.blocksdecoded.utils.inflate
import com.blocksdecoded.utils.isValidIndex
import com.blocksdecoded.utils.setHeight

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
@Deprecated("Use newer PagedPostListAdapter")
class PostListAdapter(
    private var mItems: ArrayList<PublisherPost>,
    private val mListener: PostListViewHolder.PostVHCLickListener,
    private val mLoadNextListener: LoadNextListener,
    private val mPostHeight: Int
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(),
        ListFooterViewHolder.ClickListener {

    companion object {
        private val POST = 1
        private val FOOTER = 2
    }

    private var mAlreadyLoading = false
    private val mAllLoaded = false
    private var mFooterView: ListFooterViewHolder? = null

    //region Override

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder = when (p1) {
        1 -> PostListViewHolder(p0.inflate(R.layout.adapter_post_list_item)!!, mListener).apply {
            setHeight(mPostHeight)
        }
        else -> ListFooterViewHolder(p0.inflate(R.layout.adapter_list_footer)!!, this)
    }

    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        if (mItems.isValidIndex(p1)) {
            when (p0) {
                is PostListViewHolder -> p0.onBind(mItems[p1])
                is ListFooterViewHolder -> {
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
        if (mFooterView != null) {
            mFooterView!!.errorView.visibility = View.GONE
            mFooterView!!.progressView.visibility = View.VISIBLE
        }

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