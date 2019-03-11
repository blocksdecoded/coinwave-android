package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.uiSubscribe

class CoinsListPresenter(
    view: CoinsListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases
) : BaseMVPPresenter<CoinsListContract.View>(view), CoinsListContract.Presenter {
    private val mCoinsCache = CoinsCache()
    private var mInitialized = false

    private val mCurrenciesObserver = object : ICoinsObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(scope) {
            mView?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: List<CoinEntity>) = launchSilent(scope) {
            updateCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(scope) {
            mView?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }
    }

    //region Private

    private fun searchCurrency(coinEntity: CoinEntity, body: (index: Int) -> Unit): Int =
            mCoinsCache.cachedCoins.findCurrency(coinEntity, body)

    private fun updateCurrency(coinEntity: CoinEntity): Int =
            mCoinsCache.updateCurrency(coinEntity)

    private fun updateCache(coins: List<CoinEntity>) {
        mCoinsCache.setCache(coins)
        mView?.showCoins(mCoinsCache.cachedCoins)
    }

    private fun getCurrencies() = launchSilent(scope) {
        mView?.showLoading()
        mCoinsUseCases.getCoins(true)
            .uiSubscribe(
                onNext = { },
                onComplete = { mView?.hideLoading() },
                onError = { mView?.showNetworkError(mCoinsCache.isEmpty()) }
            )
    }

    //endregion

    //region Contract

    override fun onResume() {
        super.onResume()
        mCoinsUseCases.addObserver(mCurrenciesObserver)
        if (!mInitialized) {
            mInitialized = true
            getCurrencies()
        }
    }

    override fun attachView(view: CoinsListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCoinsUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun getCoins() {
        getCurrencies()
    }

    override fun onCoinPick(position: Int) {
    }

    override fun onCoinClick(position: Int) {
        if (mCoinsCache.isValidIndex(position)) {
            mView?.openCoinInfo(mCoinsCache.cachedCoins[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    override fun onSortClick(sortType: ViewSortEnum) {
        mCoinsCache.updateSortType(sortType)
        mView?.showCoins(mCoinsCache.cachedCoins)
    }

    //endregion
}