package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.uiSubscribe

class CoinsListPresenter(
    override var view: CoinsListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases
) : BaseMvpPresenter<CoinsListContract.View>(), CoinsListContract.Presenter {
    private val mCoinsCache = CoinsCache().apply { currentSort = CoinsCache.CoinSortEnum.PRICE_DES }
    private var mInitialized = false

    private val mCurrenciesObserver = object : ICoinsObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: List<CoinEntity>) = launchSilent(scope) {
            updateCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }
    }

    //region Private

    private fun updateCurrency(coinEntity: CoinEntity): Int =
            mCoinsCache.updateCurrency(coinEntity)

    private fun updateCache(coins: List<CoinEntity>) {
        mCoinsCache.setCache(coins)
        refreshView()
    }

    private fun getCurrencies() = launchSilent(scope) {
        view?.showLoading()
        mCoinsUseCases.getCoins(true)
            .uiSubscribe(
                onNext = { },
                onComplete = { view?.hideLoading() },
                onError = { view?.showNetworkError(mCoinsCache.isEmpty()) }
            )
    }

    private fun refreshView() {
        view?.showCoins(mCoinsCache.cachedCoins)
        view?.showSortType(mCoinsCache.currentSort)
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

    override fun onDestroy() {
        super.onDestroy()
        mCoinsUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun getCoins() {
        getCurrencies()
    }

    override fun onCoinClick(position: Int) {
        if (mCoinsCache.isValidIndex(position)) {
            view?.openCoinInfo(mCoinsCache.cachedCoins[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    override fun onSortClick(sortType: ViewSortEnum) {
        mCoinsCache.updateSortType(sortType)
        refreshView()
    }

    //endregion
}