package com.blocksdecoded.coinwave.view.pickfavorite

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

interface PickFavoriteContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyClick(position: Int)
    }
}