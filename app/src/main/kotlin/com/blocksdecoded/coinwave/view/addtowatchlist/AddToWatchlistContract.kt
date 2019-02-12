package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.core.mvp.BaseMVPContract

interface AddToWatchlistContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyClick(position: Int)

        fun onCurrencyWatch(position: Int)
    }
}