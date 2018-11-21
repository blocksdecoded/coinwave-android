package com.makeuseof.cryptocurrency.view.postlist

import android.util.Log
import com.makeuseof.core.model.Result.Success
import com.makeuseof.core.model.onError
import com.makeuseof.core.model.onSuccess
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.domain.usecases.postlist.PostUseCases
import com.makeuseof.utils.Lg
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class PostListPresenter(
        view: PostListContract.View?,
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

    override fun onPostClick(position: Int) = launchSilent(ui){

    }

    override fun getPosts() = launchSilent(ui) {
        mView?.showLoading()
        mPostUseCases.getPosts()
                ?.onSuccess {
                    mView?.stopLoading()
                    mView?.showPosts(it)
                }
                ?.onError {
                    mView?.showMessage("Posts loading fail.")
                    Lg.d(it.message)
                }
    }

    override fun getNextPosts()  = launchSilent(ui){
        mPostUseCases.getPosts()
                ?.onSuccess { mView?.nextPosts(it) }
                ?.onError { mView?.showMessage("Next loading fail.") }
    }

    //endregion
}