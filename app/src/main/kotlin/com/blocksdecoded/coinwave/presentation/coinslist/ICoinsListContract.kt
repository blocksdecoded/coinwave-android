package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpContract
import java.util.*

interface ICoinsListContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun showSortType(sortType: CoinsCache.CoinSortEnum)

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun showNetworkError(hideList: Boolean)

        fun hideList()

        fun showList()

        fun showLoading()

        fun showProgress()

        fun hideProgress()

        fun hideLoading()

        fun showLastUpdated(date: Date)

        fun hideLastUpdated()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun getCoins()

        fun onMenuClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}