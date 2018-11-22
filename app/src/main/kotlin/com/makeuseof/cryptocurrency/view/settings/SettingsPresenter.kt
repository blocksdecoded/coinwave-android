package com.makeuseof.cryptocurrency.view.settings

import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.view.settings.SettingsContract

class SettingsPresenter(
        view: SettingsContract.View?
) : BaseMVPPresenter<SettingsContract.View>(view), SettingsContract.Presenter {
    override fun attachView(view: SettingsContract.View) {
        mView = view
        injectSelfToView()
    }
}