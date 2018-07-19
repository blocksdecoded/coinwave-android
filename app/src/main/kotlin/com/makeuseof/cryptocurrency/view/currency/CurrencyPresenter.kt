package com.makeuseof.cryptocurrency.view.currency

import com.makeuseof.core.mvp.BaseMVPPresenter

class CurrencyPresenter(
        view: CurrencyContract.View?
) : BaseMVPPresenter<CurrencyContract.View>(view), CurrencyContract.Presenter {
    override fun attachView(view: CurrencyContract.View) {
        mView = view
        injectSelfToView()
    }
}