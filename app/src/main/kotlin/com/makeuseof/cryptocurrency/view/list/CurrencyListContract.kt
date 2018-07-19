package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface CurrencyListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun showNetworkError()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onPickCurrency(position: Int)

        fun onCurrencyClick(position: Int)
    }
}