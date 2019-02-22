package com.blocksdecoded.coinwave.view.pickfavorite

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CoinEntity

interface PickFavoriteContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCoinClick(position: Int)
    }
}