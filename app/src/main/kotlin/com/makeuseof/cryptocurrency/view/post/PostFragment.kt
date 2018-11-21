package com.makeuseof.cryptocurrency.view.post

import android.view.View
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R

open class PostFragment : BaseMVPFragment<PostContract.Presenter>(), PostContract.View {
    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }

    override var mPresenter: PostContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_post_view

    override fun initView(rootView: View) {

    }
}