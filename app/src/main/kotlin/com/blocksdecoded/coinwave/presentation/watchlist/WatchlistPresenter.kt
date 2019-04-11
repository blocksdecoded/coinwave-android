package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
import com.blocksdecoded.coinwave.domain.usecases.watchlist.IWatchlistUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.IFavoriteChartUseVariant
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.uiSubscribe

class WatchlistPresenter(
    override var view: IWatchlistContract.View?,
    private val mMenuListener: IMenuClickListener,
    private val mWatchlistUseCases: IWatchlistUseCases,
    private val mFavoriteChartUseVariant: IFavoriteChartUseVariant
) : BaseMvpPresenter<IWatchlistContract.View>(), IWatchlistContract.Presenter {

    private val mCoinsCache = CoinsCache()

    init {
        mWatchlistUseCases.watchlistObservable
            .uiSubscribe { setCache(it.coins) }
            .addDisposable()
    }

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCurrencies(skipCache = false, force = false)
    }

    //endregion

    //region Private

    private fun refreshView() = launchSilent(scope) {
        view?.showCoins(mCoinsCache.coins)
        view?.showSortType(mCoinsCache.currentSort)
    }

    private fun setCache(coins: List<CoinEntity>) = launchSilent(scope) {
        mCoinsCache.setCache(coins)

        refreshView()

        if (mCoinsCache.isEmpty()) view?.showEmpty() else view?.hideEmpty()
    }

    private fun loadFavorite() {
        view?.showFavoriteLoading()
        view?.hideFavoriteError()

        mFavoriteChartUseVariant.getCoin()?.let {
            view?.showFavoriteCoin(it)
        }

        mFavoriteChartUseVariant.chart
            ?.uiSubscribe(
                    onNext = { view?.showFavoriteChart(it) },
                    onError = { view?.showFavoriteError() },
                    onComplete = { view?.hideFavoriteLoading() })
            .addDisposable()
    }

    private fun getCurrencies(skipCache: Boolean, force: Boolean) {
        view?.showCoinsLoading()

        if (mCoinsCache.isEmpty()) {
            view?.showFavoriteLoading()
        }

        mWatchlistUseCases.getCoins(skipCache, force)
            .uiSubscribe(
                onNext = ::onCoinsLoad,
                onError = ::onCoinsLoadError,
                onComplete = ::onCoinsLoadComplete
            )
            .addDisposable()
    }

    private fun onCoinsLoadComplete() = launchSilent(scope) {
        view?.hideCoinsLoading()
        loadFavorite()
    }

    private fun onCoinsLoad(result: CoinsResult) {
        setCache(result.coins)
    }

    private fun onCoinsLoadError(t: Throwable) {
        view?.hideCoinsLoading()
        view?.showError(mCoinsCache.isEmpty())

        if (mCoinsCache.isEmpty()) {
            view?.hideFavoriteLoading()
            view?.showFavoriteError()
        }
    }

    //endregion

    //region Contract

    override fun getCoins() {
        getCurrencies(skipCache = true, force = true)
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