package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.mvp.BaseMVPContract

interface CurrencyListContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}