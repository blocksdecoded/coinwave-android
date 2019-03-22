package com.blocksdecoded.coinwave.presentation.posts

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.contracts.LoadNextListener
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.presentation.posts.recycler.deprecated.PostListAdapter
import com.blocksdecoded.coinwave.presentation.posts.recycler.PostItemViewHolder
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.customtabs.openUrl
import com.blocksdecoded.utils.extensions.*
import com.blocksdecoded.utils.showShortToast
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

open class PostsFragment :
        BaseMvpFragment<IPostsContract.Presenter>(),
        IPostsContract.View,
        PostItemViewHolder.PostVHCLickListener,
        LoadNextListener {

    override val presenter: IPostsContract.Presenter by inject { parametersOf(this@PostsFragment, context) }
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
            R.id.post_menu -> presenter.onMenuClick()

            R.id.connection_error_retry -> presenter.getPosts()
        }
    }

    //region Init

    override fun initView(rootView: View) {
        initRecycler()

        context?.also {
            mMenuBtn.setConstraintTopMargin(it.statusBarHeight + (mMenuBtn.layoutParams as ConstraintLayout.LayoutParams).topMargin)
        }

        mSwipeRefresh.setOnRefreshListener {
            presenter.getPosts()
        }
    }

    private fun initRecycler() {
        val postHeight = ((context?.screenHeight ?: 0) * 0.27).roundToInt()

        mRecycler.setHasFixedSize(true)
        mAdapter = PostListAdapter(arrayListOf(), this, this, postHeight)
        mRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.adapter = mAdapter
    }

    //endregion

    //region Click

    override fun onClick(position: Int) {
        mAdapter?.getItem(position)?.also {
            presenter.onPostClick(it.id)
        }
    }

    //endregion

    //region Load next

    override fun onLoadNext() {
        presenter.getNextPosts()
    }

    //endregion

    //region Contract

    override fun openPost(url: String) = openUrl(url)

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

    companion object {
        fun newInstance() = PostsFragment()
    }
}
