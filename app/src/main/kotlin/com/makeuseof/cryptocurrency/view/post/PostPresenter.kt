package com.makeuseof.cryptocurrency.view.post

import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.domain.usecases.postlist.PostUseCases
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class PostPresenter(
        view: PostContract.View?,
        private val mPostUseCases: PostUseCases,
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