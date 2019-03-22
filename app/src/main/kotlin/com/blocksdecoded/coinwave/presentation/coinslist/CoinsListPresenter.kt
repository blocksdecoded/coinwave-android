package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.coroutines.launch
import java.util.*

class CoinsListPresenter(
    override var view: ICoinsListContract.View?,
    private val mMenuListener: IMenuClickListener,
    private val mCoinsUseCases: ICoinsUseCases
) : BaseMvpPresenter<ICoinsListContract.View>(), ICoinsListContract.Presenter {
    private val mCoinsCache = CoinsCache()
    private var mInitialized = false

    private val mCurrenciesObserver = object : ICoinsObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: CoinsResult) = launchSilent(scope) {
            updateCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }
    }

    //region Private

    private fun updateCurrency(coinEntity: CoinEntity): Int =
            mCoinsCache.updateCurrency(coinEntity)

    private fun updateCache(coins: CoinsResult) {
        view?.showList()
        view?.showLastUpdated(coins.lastUpdated)
        mCoinsCache.setCache(coins.coins)
        refreshView()
    }

    private fun getCurrencies() {
        if (mCoinsCache.isEmpty()) {
            view?.showLoading()
            view?.hideList()
            view?.hideProgress()
        } else {
            view?.showProgress()
            view?.hideLoading()
        }
        mCoinsUseCases.getCoins(true)
            .doOnComplete { scope.launch { view?.hideProgress() } }
            .uiSubscribe(
                onNext = {
                    view?.hideLoading()
                    updateCache(it)
                },
                onError = {
                    view?.hideLoading()
                    view?.hideProgress()
                    if (mCoinsCache.isEmpty()) view?.hideList() else view?.showNetworkError()
                }
            ).let { disposables.add(it) }
    }

    private fun refreshView() {
        view?.showCoins(mCoinsCache.coins)
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
            view?.openCoinInfo(mCoinsCache.coins[position].id)
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