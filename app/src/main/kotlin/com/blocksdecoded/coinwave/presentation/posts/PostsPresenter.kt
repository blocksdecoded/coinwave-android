package com.blocksdecoded.coinwave.presentation.posts

import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.observeUi

class PostsPresenter(
    override var view: IPostsContract.View?,
    private val mMenuListener: IMenuClickListener,
    private val mPostUseCases: IPostsUseCases
) : BaseMvpPresenter<IPostsContract.View>(), IPostsContract.Presenter {

    private var mInitialized = false

    override fun onResume() {
        super.onResume()
        if (!mInitialized) {
            mInitialized = true
            getPosts()
        }
    }

    //region Contract

    override fun onPostClick(id: Int) = launchSilent(scope) {
        mPostUseCases.getPost(id)?.also {
            it.url?.also {
                view?.openPost(it)
            }
        }
    }

    override fun getPosts() = launchSilent(scope) {
        view?.showLoading()
        mPostUseCases.getPosts()
            .observeUi()
            .subscribe(
                { view?.showPosts(it) },
                { view?.showLoadingError() },
                { view?.stopLoading() }
            ).let { disposables.add(it) }
    }

    override fun getNextPosts() = launchSilent(scope) {
        mPostUseCases.getNextPosts()
            .observeUi()
            .subscribe(
                { view?.nextPosts(it) },
                { view?.showErrorMessage() }
            ).let { disposables.add(it) }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}