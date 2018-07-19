package com.makeuseof.cryptocurrency.view.currency

import com.makeuseof.core.mvp.BaseMVPContract

interface CurrencyContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}