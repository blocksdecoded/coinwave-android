package com.makeuseof.cryptocurrency.view.postlist

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.OnClick
import com.makeuseof.core.contracts.LoadNextListener
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.cryptocurrency.view.post.PostActivity
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListAdapter
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListViewHolder
import com.makeuseof.utils.DimenUtils
import com.makeuseof.utils.extensions.setConstraintTopMargin
import com.makeuseof.utils.hide
import com.makeuseof.utils.visible
import kotlin.math.roundToInt

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
    lateinit var mRecycler: RecyclerView
    @BindView(R.id.fragment_post_list_swipe_refresh)
    lateinit var mSwipeRefresh: SwipeRefreshLayout
    @BindView(R.id.post_menu)
    lateinit var mMenuBtn: View

    @OnClick(R.id.post_menu)
    fun onClick(view: View) {
        when(view.id) {
            R.id.post_menu -> mPresenter?.onMenuClick()
        }
    }

    var mAdapter: PostListAdapter? = null

    //region Init

    override fun initView(rootView: View) {
        initRecycler(rootView)

        context?.also {
            mMenuBtn.setConstraintTopMargin(DimenUtils.getStatusBarHeight(it) + DimenUtils.dpToPx(it, 12))
        }

        mSwipeRefresh.setOnRefreshListener {
            mPresenter?.getPosts()
        }
    }

    private fun initRecycler(rootView: View?) {
        var postHeight = 0
        activity?.also {
            val metrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(metrics)
            postHeight = (metrics.heightPixels * 0.35).roundToInt()
        }

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

    override fun openPost(postId: Int) {
        activity?.also {
            PostActivity.start(it, postId)
//            it.startActivity(
//                    Intent(it, PostViewActivity::class.java)
//                            .putExtra(CoreSharedConstants.KEY_POST_ID, postId.toString())
//            )
//            activity?.overridePendingTransition(R.anim.slide_in_right, 0)
        }
    }

    override fun showLoading() {
        mSwipeRefresh.isRefreshing = true
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

    //endregion
}
