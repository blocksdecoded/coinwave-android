package com.makeuseof.cryptocurrency.view.settings

import com.makeuseof.core.mvp.BaseMVPContract

interface SettingsContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}