package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.extensions.isValidIndex

class CoinsListPresenter(
    view: CoinsListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases
) : BaseMVPPresenter<CoinsListContract.View>(view), CoinsListContract.Presenter {
    private var mCachedData = arrayListOf<CoinEntity>()
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
            mCachedData.findCurrency(coinEntity, body)

    private fun updateCurrency(coinEntity: CoinEntity): Int =
            searchCurrency(coinEntity) {
                mCachedData[it] = coinEntity
            }

    private fun removeCurrency(coinEntity: CoinEntity): Int =
            searchCurrency(coinEntity) {
                mCachedData.removeAt(it)
            }

    private fun updateCache(coins: List<CoinEntity>) {
        mCachedData.clear()
        mCachedData.addAll(coins)
        mView?.showCoins(mCachedData)
    }

    private fun getCurrencies() = launchSilent(scope) {
        mView?.showLoading()
        mCoinsUseCases.getCoins(true)
                .onSuccess { mView?.hideLoading() }
                .onError {
                    mView?.hideLoading()
                    mView?.showNetworkError(mCachedData.isEmpty())
                }
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

    override fun deleteCoin(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            if (mCachedData[position].isSaved) {
                mView?.showMessage("${mCachedData[position].name} removed from Watchlist")
                mCoinsUseCases.removeCoin(mCachedData[position].id)
            }
        }
    }

    override fun onCoinClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            mView?.openCoinInfo(mCachedData[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}