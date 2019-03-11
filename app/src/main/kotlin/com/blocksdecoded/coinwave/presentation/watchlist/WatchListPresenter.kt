package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.extensions.isValidIndex
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.coroutines.launch

class WatchListPresenter(
    view: WatchListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases,
    private val mFavoriteChartUseVariant: FavoriteChartUseVariant
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    private var mCachedData = arrayListOf<CoinEntity>()

    private val mCurrenciesObserver = object : ICoinsObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(scope) {
            mView?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: List<CoinEntity>) = launchSilent(scope) {
            setCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(scope) {
            mView?.deleteCoin(removeCurrency(coinEntity))
            if (mCachedData.isEmpty()) mView?.showEmpty()
        }
    }

    //region Lifecycle

    override fun attachView(view: WatchListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCoinsUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun onResume() {
        super.onResume()
        mCoinsUseCases.addObserver(mCurrenciesObserver)
        getCurrencies(false)
    }

    //endregion

    //region Private

    private fun setCache(coins: List<CoinEntity>) = launchSilent(scope) {
        mCachedData.clear()
        mCachedData.addAll(coins.filter { it.isSaved })

        mView?.showCoins(mCachedData)

        if (mCachedData.isEmpty()) mView?.showEmpty() else mView?.hideEmpty()

        loadFavorite()
    }

    private suspend fun loadFavorite() {
        mView?.showFavoriteLoading()
        mView?.hideFavoriteError()

        mFavoriteChartUseVariant.getCoin()
            ?.onSuccess { mView?.showFavoriteCoin(it) }

        mFavoriteChartUseVariant.chart
            ?.doAfterTerminate { scope.launch { } }
            ?.uiSubscribe(
                    onNext = { mView?.showFavoriteChart(it) },
                    onError = { mView?.showFavoriteError() },
                    onComplete = { mView?.hideFavoriteLoading() }
            )?.let { disposables.add(it) }
    }

    private fun searchCurrency(coinEntity: CoinEntity, body: ((index: Int) -> Unit)? = null): Int =
            mCachedData.findCurrency(coinEntity, body)

    private fun updateCurrency(coinEntity: CoinEntity): Int {
        searchCurrency(coinEntity).also {
            if (it == -1) {
                mCachedData.addSortedByRank(coinEntity)
            }
        }

        return 0
    }

    private fun removeCurrency(coinEntity: CoinEntity): Int = searchCurrency(coinEntity) {
        mCachedData.removeAt(it)
    }

    private fun getCurrencies(skipCache: Boolean) = launchSilent(scope) {
        mView?.showCoinsLoading()
        if (mCachedData.isEmpty()) {
            mView?.showFavoriteLoading()
        }

        mCoinsUseCases.getCoins(skipCache)
                .map { it.filter { it.isSaved } }
                .uiSubscribe(
                        onNext = { setCache(it) },
                        onError = {
                            mView?.showError(mCachedData.isEmpty())

                            if (mCachedData.isEmpty()) {
                                mView?.hideFavoriteLoading()
                                mView?.showFavoriteError()
                            }
                        },
                        onComplete = { mView?.hideCoinsLoading() }
                )
    }

    //endregion

    //region Contract

    override fun getCoins() {
        getCurrencies(true)
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

    override fun onAddCoinClick() {
        mView?.openAddToWatchlist()
    }

    //endregion
}