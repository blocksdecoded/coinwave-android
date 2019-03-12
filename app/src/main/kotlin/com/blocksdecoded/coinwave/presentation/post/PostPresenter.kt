package com.blocksdecoded.coinwave.presentation.post

import com.blocksdecoded.core.mvp.deprecated.BaseMVPPresenter
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsUseCases
import com.blocksdecoded.utils.coroutine.launchSilent

class PostPresenter(
    view: PostContract.View?,
    private val mPostUseCases: PostsUseCases
) : BaseMVPPresenter<PostContract.View>(view), PostContract.Presenter {
    override fun attachView(view: PostContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun getPost(id: Int) = launchSilent(scope) {
        mPostUseCases.getPost(id)?.also {
            mView?.showPost(it)
        }
    }
}