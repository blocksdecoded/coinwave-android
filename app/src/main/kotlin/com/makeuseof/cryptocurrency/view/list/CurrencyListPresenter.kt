package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.mvp.BaseMVPPresenter

class CurrencyListPresenter(
        view: CurrencyListContract.View?
) : BaseMVPPresenter<CurrencyListContract.View>(view), CurrencyListContract.Presenter {
    override fun attachView(view: CurrencyListContract.View) {
        mView = view
        injectSelfToView()
    }
}