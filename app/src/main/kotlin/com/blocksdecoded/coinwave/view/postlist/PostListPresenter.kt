package com.blocksdecoded.coinwave.view.postlist

import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.domain.usecases.postlist.PostUseCases
import com.blocksdecoded.coinwave.view.main.MenuClickListener
import com.blocksdecoded.utils.Lg
import com.blocksdecoded.utils.coroutine.launchSilent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class PostListPresenter(
        view: PostListContract.View?,
        private val mMenuListener: MenuClickListener,
        private val mPostUseCases: PostUseCases,
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

    override fun onPostClick(id: Int) = launchSilent(ui){
        mPostUseCases.getPost(id)?.also {
            it.url?.also {
                mView?.openPost(it)
            }
        }
    }

    override fun getPosts() = launchSilent(ui) {
        mView?.showLoading()
        mPostUseCases.getPosts()
                ?.onSuccess {
                    mView?.stopLoading()
                    mView?.showPosts(it)
                }
                ?.onError { Lg.d(it.message) }
    }

    override fun getNextPosts()  = launchSilent(ui){
        mPostUseCases.getNextPosts()
                ?.onSuccess { mView?.nextPosts(it) }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}