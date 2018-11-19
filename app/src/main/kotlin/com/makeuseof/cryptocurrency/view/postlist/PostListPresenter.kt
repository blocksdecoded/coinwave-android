package com.makeuseof.cryptocurrency.view.postlist

import com.makeuseof.core.mvp.BaseMVPPresenter

class PostListPresenter(
        view: PostListContract.View?
) : BaseMVPPresenter<PostListContract.View>(view), PostListContract.Presenter {
    override fun attachView(view: PostListContract.View) {
        mView = view
        injectSelfToView()
    }

    //region Contract

    override fun onPostClick(position: Int) {

    }

    //endregion
}