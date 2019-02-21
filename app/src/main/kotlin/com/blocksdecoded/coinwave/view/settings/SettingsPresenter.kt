package com.blocksdecoded.coinwave.view.settings

import com.blocksdecoded.core.mvp.BaseMVPPresenter

class SettingsPresenter(
    view: SettingsContract.View?
) : BaseMVPPresenter<SettingsContract.View>(view), SettingsContract.Presenter {
    override fun attachView(view: SettingsContract.View) {
        mView = view
        injectSelfToView()
    }
}