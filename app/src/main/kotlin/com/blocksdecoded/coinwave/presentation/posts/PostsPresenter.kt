package com.blocksdecoded.coinwave.presentation.posts

import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.rx.uiSubscribe

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

    override fun onPostClick(id: Int) {
        mPostUseCases.getPost(id)?.also {
            it.url?.also {
                view?.openPost(it)
            }
        }
    }

    override fun getPosts() {
        view?.showLoading()
        mPostUseCases.getPosts()
            .uiSubscribe(
                { view?.showPosts(it) },
                { view?.showLoadingError() },
                { view?.stopLoading() })
            .addDisposable()
    }

    override fun getNextPosts() {
        mPostUseCases.getNextPosts()
            .uiSubscribe(
                { view?.nextPosts(it) },
                { view?.showErrorMessage() })
            .addDisposable()
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}