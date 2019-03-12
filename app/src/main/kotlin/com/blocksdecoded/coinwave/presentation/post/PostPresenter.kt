package com.blocksdecoded.coinwave.presentation.post

import com.blocksdecoded.core.mvp.deprecated.BaseMVPPresenter
import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.utils.coroutine.launchSilent

class PostPresenter(
        view: IPostContract.View?,
        private val mPostUseCases: IPostsUseCases
) : BaseMVPPresenter<IPostContract.View>(view), IPostContract.Presenter {
    override fun attachView(view: IPostContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun getPost(id: Int) = launchSilent(scope) {
        mPostUseCases.getPost(id)?.also {
            mView?.showPost(it)
        }
    }
}