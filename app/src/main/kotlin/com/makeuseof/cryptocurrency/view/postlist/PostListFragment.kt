package com.makeuseof.cryptocurrency.view.postlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.view.postlist.recycler.PostListAdapter
import com.makeuseof.utils.inflate

class PostListFragment : BaseMVPFragment<PostListContract.Presenter>(), PostListContract.View {
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
    }

    private fun initRecycler(rootView: View?){
        mAdapter = PostListAdapter()
        mRecycler = rootView?.findViewById(R.id.fragment_post_list_recycler)
        mRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.adapter = mAdapter
    }

    //endregion
}