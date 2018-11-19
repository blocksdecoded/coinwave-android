package com.makeuseof.cryptocurrency.view.postlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.post.GetPostsRequest
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListAdapter
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListViewHolder
import com.makeuseof.muocore.CoreSharedConstants
import com.makeuseof.muocore.backend.ApiRequest
import com.makeuseof.muocore.backend.GetPostRequest
import com.makeuseof.muocore.models.PublisherPost
import com.makeuseof.muocore.ui.PostViewActivity
import com.makeuseof.utils.inflate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PostListFragment :
        BaseMVPFragment<PostListContract.Presenter>(),
        PostListContract.View,
        PostListViewHolder.PostVHCLickListener
{
    companion object {
        fun newInstance(): PostListFragment = PostListFragment()
    }

    override var mPresenter: PostListContract.Presenter? = null

    private var mRecycler: RecyclerView? = null
    private var mAdapter: PostListAdapter? = null

    //region Lifecycle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_post_list)

        init(rootView)

        return rootView
    }

    //endregion

    //region Init

    private fun init(rootView: View?) {
        initRecycler(rootView)

        try {
            ApiRequest.apiClient.perform(object: GetPostsRequest(context, null){
                override fun onSuccess(posts: MutableList<PublisherPost>?) {
                    Log.d("ololo", "Received posts ${posts?.size}")
                    posts?.also { it ->
                        showPosts(posts.map { it })
                    }
                }
            })
        } catch (e: Exception) {
        }
    }

    private fun initRecycler(rootView: View?) {
        mAdapter = PostListAdapter(arrayListOf(), this)
        mRecycler = rootView?.findViewById(R.id.fragment_post_list_recycler)
        mRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.adapter = mAdapter
    }

    //endregion

    //region Private

    private fun showPosts(posts: List<PublisherPost>) = GlobalScope.async(Dispatchers.Main) {
        mAdapter?.setItems(posts)
    }

    //endregion

    //region Click

    override fun onClick(position: Int) {
        Log.d("ololo", "On post click $position")
        mAdapter?.getItem(position)?.also {
            openPost(it.id)
        }
    }

    //endregion

    //region Contract

    override fun openPost(postId: Int) {
        activity?.also {
            Log.d("ololo", "Open post $postId")
            it.startActivity(
                    Intent(it, PostViewActivity::class.java)
                            .putExtra(CoreSharedConstants.KEY_POST_ID, postId.toString())
            )
            activity?.overridePendingTransition(R.anim.slide_in_right, 0)
        }
    }

    //endregion
}