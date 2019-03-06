package com.blocksdecoded.coinwave.presentation.postlist

import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.contracts.LoadNextListener
import com.blocksdecoded.core.mvp.BaseMVPFragment
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.presentation.postlist.recycler.deprecated.PostListAdapter
import com.blocksdecoded.coinwave.presentation.postlist.recycler.PostListViewHolder
import com.blocksdecoded.utils.DimenUtils
import com.blocksdecoded.utils.customtabs.CustomTabsUtil
import com.blocksdecoded.utils.extensions.setConstraintTopMargin
import com.blocksdecoded.utils.hide
import com.blocksdecoded.utils.showShortToast
import com.blocksdecoded.utils.visible
import kotlin.math.roundToInt

open class PostListFragment :
        BaseMVPFragment<PostListContract.Presenter>(),
        PostListContract.View,
        PostListViewHolder.PostVHCLickListener,
        LoadNextListener {
    companion object {
        fun newInstance() = PostListFragment()
    }

    override var mPresenter: PostListContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_post_list

    var mAdapter: PostListAdapter? = null
    @BindView(R.id.fragment_post_list_recycler)
    lateinit var mRecycler: RecyclerView
    @BindView(R.id.fragment_post_list_swipe_refresh)
    lateinit var mSwipeRefresh: SwipeRefreshLayout
    @BindView(R.id.post_menu)
    lateinit var mMenuBtn: View

    @BindView(R.id.fragment_post_list_error)
    lateinit var mErrorView: View

    @OnClick(
            R.id.post_menu,
            R.id.connection_error_retry
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.post_menu -> mPresenter?.onMenuClick()

            R.id.connection_error_retry -> mPresenter?.getPosts()
        }
    }

    //region Init

    override fun initView(rootView: View) {
        initRecycler()

        context?.also {
            mMenuBtn.setConstraintTopMargin(DimenUtils.getStatusBarHeight(it) + (mMenuBtn.layoutParams as ConstraintLayout.LayoutParams).topMargin)
        }

        mSwipeRefresh.setOnRefreshListener {
            mPresenter?.getPosts()
        }
    }

    private fun initRecycler() {
        var postHeight = 0
        activity?.also {
            val metrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(metrics)
            postHeight = (metrics.heightPixels * 0.27).roundToInt()
        }

        mRecycler.setHasFixedSize(true)
        mAdapter = PostListAdapter(arrayListOf(), this, this, postHeight)
        mRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.adapter = mAdapter
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

    override fun openPost(url: String) {
        activity?.let {
            CustomTabsUtil.openUrl(it, url)
        }
    }

    override fun showLoading() {
        mSwipeRefresh.isRefreshing = true
        mErrorView.hide()
        mRecycler.hide()
    }

    override fun stopLoading() {
        mSwipeRefresh.isRefreshing = false
        mRecycler.visible()
    }

    override fun showPosts(posts: List<PublisherPost>) {
        mAdapter?.setItems(posts)
    }

    override fun nextPosts(posts: List<PublisherPost>) {
        mAdapter?.addItems(posts)
    }

    override fun showLoadingError() {
        mSwipeRefresh.isRefreshing = false
        mRecycler.hide()
        mErrorView.visible()
    }

    override fun showErrorMessage() {
        showShortToast(context, getString(R.string.message_connection_error))
    }

    //endregion
}
