package com.blocksdecoded.coinwave.presentation.posts

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.contracts.LoadNextListener
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.presentation.posts.recycler.PostsAdapter
import com.blocksdecoded.coinwave.presentation.posts.recycler.PostViewHolder
import com.blocksdecoded.core.mvvm.CoreMvvmFragment
import com.blocksdecoded.utils.customtabs.openUrl
import com.blocksdecoded.utils.extensions.*
import com.blocksdecoded.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

open class PostsFragment :
        CoreMvvmFragment<PostsViewModel>(),
        PostViewHolder.PostVHCLickListener,
        LoadNextListener {

    override val mViewModel: PostsViewModel by viewModel { parametersOf(context) }
    override val layoutId: Int = R.layout.fragment_post_list

    var mAdapter: PostsAdapter? = null
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
            R.id.post_menu -> mViewModel.onMenuClick()

            R.id.connection_error_retry -> mViewModel.onRetryClick()
        }
    }

    //region Init

    override fun initView(rootView: View) {
        initRecycler()

        context?.also {
            val menuLayoutParams = mMenuBtn.layoutParams
            if (menuLayoutParams is ConstraintLayout.LayoutParams) {
                mMenuBtn.setConstraintTopMargin(it.statusBarHeight + menuLayoutParams.topMargin)
            }
        }

        mSwipeRefresh.setOnRefreshListener {
            mViewModel.getPosts()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.posts.observe(this, Observer { mAdapter?.setItems(it) })

        mViewModel.openPostEvent.observe(this, Observer { it.url?.let { openUrl(it) } })

        mViewModel.allLoaded.observe(this, Observer { mAdapter?.setAllLoaded(it) })

        mViewModel.isLoading.observe(this, Observer {
            if (it) {
                mSwipeRefresh.isRefreshing = true
                mErrorView.hide()
                mRecycler.hide()
            } else {
                mSwipeRefresh.isRefreshing = false
                mRecycler.visible()
            }
        })

        mViewModel.loadingErrorEvent.observe(this, Observer { showView ->
            if (showView) {
                mSwipeRefresh.isRefreshing = false
                mRecycler.hide()
                mErrorView.visible()
            } else {
                showShortToast(context, getString(R.string.message_connection_error))
            }
        })
    }

    private fun initRecycler() {
        val postHeight = ((context?.screenHeight ?: 0) * 0.27).roundToInt()

        mRecycler.setHasFixedSize(true)
        mAdapter = PostsAdapter(
            arrayListOf(),
            this,
            this,
            postHeight
        )
        mRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.adapter = mAdapter
    }

    //endregion

    override fun onClick(position: Int) {
        mViewModel.onPostClick(position)
    }

    override fun onLoadNext() {
        mViewModel.getNextPosts()
    }

    companion object {
        fun newInstance() = PostsFragment()
    }
}
