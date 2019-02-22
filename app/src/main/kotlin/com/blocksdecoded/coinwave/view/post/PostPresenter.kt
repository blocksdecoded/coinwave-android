package com.blocksdecoded.coinwave.view.post

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsUseCases
import com.blocksdecoded.utils.coroutine.launchSilent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class PostPresenter(
    view: PostContract.View?,
    private val mPostUseCases: PostsUseCases,
    private val ui: MainCoroutineDispatcher = Dispatchers.Main
) : BaseMVPPresenter<PostContract.View>(view), PostContract.Presenter {
    override fun attachView(view: PostContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun getPost(id: Int) = launchSilent(ui) {
        mPostUseCases.getPost(id)?.also {
            mView?.showPost(it)
        }
    }
}