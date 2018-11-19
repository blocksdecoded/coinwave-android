package com.makeuseof.cryptocurrency.view.post

import com.makeuseof.core.mvp.BaseMVPPresenter

class PostPresenter(
        view: PostContract.View?
) : BaseMVPPresenter<PostContract.View>(view), PostContract.Presenter {
    override fun attachView(view: PostContract.View) {
        mView = view
        injectSelfToView()
    }
}