package com.makeuseof.cryptocurrency.view.pickfavorite

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface PickFavoriteContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyClick(position: Int)
    }
}