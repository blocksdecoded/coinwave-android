package com.makeuseof.cryptocurrency.view.pickfavorite

import com.makeuseof.core.mvp.BaseMVPPresenter

class PickFavoritePresenter(
        view: PickFavoriteContract.View?
) : BaseMVPPresenter<PickFavoriteContract.View>(view), PickFavoriteContract.Presenter {
    override fun attachView(view: PickFavoriteContract.View) {
        mView = view
        injectSelfToView()
    }
}