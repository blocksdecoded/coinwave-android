package com.makeuseof.cryptocurrency.view.postlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.makeuseof.core.contracts.LoadNextListener
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListAdapter
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListViewHolder
import com.makeuseof.muocore.CoreSharedConstants
import com.makeuseof.muocore.ui.PostViewActivity
import com.makeuseof.utils.inflate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

open class PostListFragment :
        BaseMVPFragment<PostListContract.Presenter>(),
        PostListContract.View,
        PostListViewHolder.PostVHCLickListener,
        LoadNextListener
{
    companion object {
        fun newInstance(): PostListFragment = PostListFragment()
    }

    override var mPresenter: PostListContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_post_list

    @BindView(R.id.fragment_post_list_recycler)
    @JvmField var mRecycler: RecyclerView? = null
    var mAdapter: PostListAdapter? = null

    //region Init

    override fun initView(rootView: View) {
        initRecycler(rootView)
    }

    private fun initRecycler(rootView: View?) {
        var postHeight = 0
        activity?.also {
            val metrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(metrics)
            postHeight = metrics.widthPixels * 9 / 16
        }

        mAdapter = PostListAdapter(arrayListOf(), this, this, postHeight)
        mRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.adapter = mAdapter
    }

    //endregion

    //region Click

    override fun onClick(position: Int) {
        mAdapter?.getItem(position)?.also {
            mPresenter?.onPostClick(it.id)
        }
    }

    //endregion

    //region Load next

    override fun onLoadNext() {
        mPresenter?.getNextPosts()
    }

    //endregion

    //region Contract

    override fun openPost(postId: Int) {
        activity?.also {
            it.startActivity(
                    Intent(it, PostViewActivity::class.java)
                            .putExtra(CoreSharedConstants.KEY_POST_ID, postId.toString())
            )
            activity?.overridePendingTransition(R.anim.slide_in_right, 0)
        }
    }

    override fun showLoading() {
    }

    override fun stopLoading() {
    }

    override fun showPosts(posts: List<PublisherPost>) {
        mAdapter?.setItems(posts)
    }

    override fun nextPosts(posts: List<PublisherPost>) {
        mAdapter?.addItems(posts)
    }

    //endregion
}