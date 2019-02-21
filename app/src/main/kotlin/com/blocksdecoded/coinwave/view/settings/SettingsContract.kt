package com.blocksdecoded.coinwave.view.settings

import com.blocksdecoded.core.mvp.BaseMVPContract

interface SettingsContract {

    interface View : BaseMVPContract.View<Presenter>

    interface Presenter : BaseMVPContract.Presenter<View>
}