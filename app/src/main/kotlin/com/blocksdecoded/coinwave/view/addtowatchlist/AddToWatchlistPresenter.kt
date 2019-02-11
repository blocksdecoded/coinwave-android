package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.core.mvp.BaseMVPPresenter

class AddToWatchlistPresenter(
        view: AddToWatchlistContract.View?
) : BaseMVPPresenter<AddToWatchlistContract.View>(view), AddToWatchlistContract.Presenter {
    override fun attachView(view: AddToWatchlistContract.View) {
        mView = view
        injectSelfToView()
    }
}