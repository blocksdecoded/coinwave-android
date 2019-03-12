package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.coroutines.async

class WatchListPresenter(
    override var view: WatchListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases,
    private val mFavoriteChartUseVariant: FavoriteChartUseVariant
) : BaseMvpPresenter<WatchListContract.View>(), WatchListContract.Presenter {

    private val mCoinsCache = CoinsCache()

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

    private fun refreshView() {
        view?.showCoins(mCoinsCache.coins)
        view?.showSortType(mCoinsCache.currentSort)
    }

    private fun setCache(coins: List<CoinEntity>) = launchSilent(scope) {
        mCoinsCache.setCache(coins.filter { it.isSaved })

        refreshView()

        if (mCoinsCache.isEmpty()) view?.showEmpty() else view?.hideEmpty()

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
                    onComplete = { }
            )?.let { disposables.add(it) }
    }

    private fun updateCurrency(coinEntity: CoinEntity): Int = mCoinsCache.add(coinEntity)

    private fun removeCurrency(coinEntity: CoinEntity): Int = mCoinsCache.remove(coinEntity)

    private fun getCurrencies(skipCache: Boolean) = launchSilent(scope) {
        view?.showCoinsLoading()
        if (mCoinsCache.isEmpty()) {
            view?.showFavoriteLoading()
        }

        disposables.add(
                mCoinsUseCases.getCoins(skipCache)
                        .map { it.filter { it.isSaved } }
                        .uiSubscribe(
                                onNext = { setCache(it) },
                                onError = {
                                    view?.showError(mCoinsCache.isEmpty())

                                    if (mCoinsCache.isEmpty()) {
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

    override fun deleteCoin(position: Int) {
        if (mCoinsCache.isValidIndex(position)) {
            if (mCoinsCache.coins[position].isSaved) {
                view?.showMessage("${mCoinsCache.coins[position].name} removed from Watchlist")
                mCoinsUseCases.removeCoin(mCoinsCache.coins[position].id)
            }
        }
    }

    override fun onCoinClick(position: Int) {
        if (mCoinsCache.isValidIndex(position)) {
            view?.openCoinInfo(mCoinsCache.coins[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    override fun onAddCoinClick() {
        view?.openAddToWatchlist()
    }

    override fun onSortClick(sortType: ViewSortEnum) {
        mCoinsCache.updateSortType(sortType)
        refreshView()
    }

    //endregion
}