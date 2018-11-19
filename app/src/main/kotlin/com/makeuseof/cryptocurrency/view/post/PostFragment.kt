package com.makeuseof.cryptocurrency.view.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment

class PostFragment : BaseMVPFragment<PostContract.Presenter>(), PostContract.View {
    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }

    override var mPresenter: PostContract.Presenter? = null

    //region Lifecycle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    //endregion
}