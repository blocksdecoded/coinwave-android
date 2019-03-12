package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.extensions.isValidIndex
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.coroutines.async

class WatchListPresenter(
    override var view: WatchListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases,
    private val mFavoriteChartUseVariant: FavoriteChartUseVariant
) : BaseMvpPresenter<WatchListContract.View>(), WatchListContract.Presenter {

    private var mCoinsCache = CoinsCache()
    private var mCachedData = arrayListOf<CoinEntity>()

    private val mCurrenciesObserver = object : ICoinsObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: List<CoinEntity>) = launchSilent(scope) {
            setCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(scope) {
            view?.deleteCoin(removeCurrency(coinEntity))
            if (mCoinsCache.isEmpty()) view?.showEmpty()
        }
    }

    //region Lifecycle

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

        view?.showCoins(mCachedData)

        if (mCachedData.isEmpty()) view?.showEmpty() else view?.hideEmpty()

        loadFavorite()
    }

    private suspend fun loadFavorite() {
        view?.showFavoriteLoading()
        view?.hideFavoriteError()

        mFavoriteChartUseVariant.getCoin()
            ?.onSuccess { view?.showFavoriteCoin(it) }

        mFavoriteChartUseVariant.chart
            ?.doOnComplete { scope.async { view?.hideFavoriteLoading() } }
            ?.uiSubscribe(
                    onNext = { view?.showFavoriteChart(it) },
                    onError = { view?.showFavoriteError() },
                    onComplete = {  }
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
        view?.showCoinsLoading()
        if (mCachedData.isEmpty()) {
            view?.showFavoriteLoading()
        }

        disposables.add(
                mCoinsUseCases.getCoins(skipCache)
                        .map { it.filter { it.isSaved } }
                        .uiSubscribe(
                                onNext = { setCache(it) },
                                onError = {
                                    view?.showError(mCachedData.isEmpty())

                                    if (mCachedData.isEmpty()) {
                                        view?.hideFavoriteLoading()
                                        view?.showFavoriteError()
                                    }
                                },
                                onComplete = { view?.hideCoinsLoading() }
                        )
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
                view?.showMessage("${mCachedData[position].name} removed from Watchlist")
                mCoinsUseCases.removeCoin(mCachedData[position].id)
            }
        }
    }

    override fun onCoinClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            view?.openCoinInfo(mCachedData[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    override fun onAddCoinClick() {
        view?.openAddToWatchlist()
    }

    //endregion
}