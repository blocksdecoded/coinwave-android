package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.core.mvp.BaseMVPPresenter

class WatchListPresenter(
        view: WatchListContract.View?
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    override fun attachView(view: WatchListContract.View) {
        mView = view
        injectSelfToView()
    }
}