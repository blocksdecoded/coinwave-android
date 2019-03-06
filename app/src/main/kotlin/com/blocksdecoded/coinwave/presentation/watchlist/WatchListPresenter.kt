package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.crypto.CoinsUpdateObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onResult
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class WatchListPresenter(
    view: WatchListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCoinsUseCases: CoinsUseCases,
    private val mFavoriteChartUseVariant: FavoriteChartUseVariant,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    private var mCachedData = arrayListOf<CoinEntity>()

    private val mCurrenciesObserver = object : CoinsUpdateObserver {
        override fun onAdded(coinEntity: CoinEntity) = launchSilent(uiContext) {
            mView?.updateCoin(updateCurrency(coinEntity), coinEntity)
        }

        override fun onUpdated(coins: List<CoinEntity>) = launchSilent(uiContext) {
            setCache(coins)
        }

        override fun onRemoved(coinEntity: CoinEntity) = launchSilent(uiContext) {
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

    private fun setCache(coins: List<CoinEntity>) = launchSilent(uiContext) {
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

        mFavoriteChartUseVariant.getChart()
            ?.onResult { mView?.hideFavoriteLoading() }
            ?.onSuccess { mView?.showFavoriteChart(it) }
            ?.onError { mView?.showFavoriteError() }
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

    private fun getCurrencies(skipCache: Boolean) = launchSilent(uiContext) {
        mView?.showCoinsLoading()
        if (mCachedData.isEmpty()) {
            mView?.showFavoriteLoading()
        }

        mCoinsUseCases.getCoins(skipCache)
            .onResult { mView?.hideCoinsLoading() }
            .onSuccess { setCache(it) }
            .onError {
                mView?.showError(mCachedData.isEmpty())

                if (mCachedData.isEmpty()) {
                    mView?.hideFavoriteLoading()
                    mView?.showFavoriteError()
                }
            }
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