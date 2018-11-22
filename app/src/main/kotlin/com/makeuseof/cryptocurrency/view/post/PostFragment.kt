package com.makeuseof.cryptocurrency.view.post

import android.view.View
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

open class PostFragment : BaseMVPFragment<PostContract.Presenter>(), PostContract.View {
    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }

    override var mPresenter: PostContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_post_view

    override fun initView(rootView: View) {

    }

    //region Contract

    override fun showPost(post: PublisherPost) {

    }

    //endregion
}