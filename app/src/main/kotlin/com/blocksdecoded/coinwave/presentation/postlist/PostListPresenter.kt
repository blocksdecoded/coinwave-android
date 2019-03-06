package com.blocksdecoded.coinwave.presentation.postlist

import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsUseCases
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class PostListPresenter(
    view: PostListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mPostUseCases: PostsUseCases,
    private val ui: MainCoroutineDispatcher = Dispatchers.Main
) : BaseMVPPresenter<PostListContract.View>(view), PostListContract.Presenter {

    private var mInitialized = false

    override fun attachView(view: PostListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onResume() {
        super.onResume()
        if (!mInitialized) {
            mInitialized = true
            getPosts()
        }
    }

    //region Contract

    override fun onPostClick(id: Int) = launchSilent(ui) {
        mPostUseCases.getPost(id)?.also {
            it.url?.also {
                mView?.openPost(it)
            }
        }
    }

    override fun getPosts() = launchSilent(ui) {
        mView?.showLoading()
        mPostUseCases.getPosts()
            ?.onResult { mView?.stopLoading() }
            ?.onSuccess { mView?.showPosts(it) }
            ?.onError { mView?.showLoadingError() }
    }

    override fun getNextPosts() = launchSilent(ui) {
        mPostUseCases.getNextPosts()
                ?.onSuccess { mView?.nextPosts(it) }
                ?.onError { mView?.showErrorMessage() }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}