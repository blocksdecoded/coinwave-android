package com.blocksdecoded.coinwave.view.watchlist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.NetworkException
import com.blocksdecoded.coinwave.data.crypto.CurrencyUpdateObserver
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.view.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import com.blocksdecoded.utils.logD
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class WatchListPresenter(
    view: WatchListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCurrencyListUseCases: CurrencyListUseCases,
    private val mFavoriteChartUseVariant: FavoriteChartUseVariant,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    private var mCachedData = arrayListOf<CurrencyEntity>()

    private val mCurrenciesObserver = object : CurrencyUpdateObserver {
        override fun onAdded(currencyEntity: CurrencyEntity) = launchSilent(uiContext) {
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }

        override fun onUpdated(currencies: List<CurrencyEntity>) = launchSilent(uiContext) {
            setCache(currencies)
        }

        override fun onRemoved(currencyEntity: CurrencyEntity) = launchSilent(uiContext) {
            mView?.deleteCurrency(removeCurrency(currencyEntity))
            if (mCachedData.isEmpty()) mView?.showEmpty()
        }
    }

    //region Private

    private fun setCache(currencies: List<CurrencyEntity>) = launchSilent(uiContext) {
        mCachedData.clear()
        mCachedData.addAll(currencies.filter { it.isSaved })
        mView?.showCurrencies(mCachedData)

        if (mCachedData.isEmpty()) mView?.showEmpty()

        mView?.showFavoriteLoading()

        mFavoriteChartUseVariant.getCurrency()?.onSuccess {
            mView?.showFavoriteCurrency(it)
        }?.onError {
            logD("Failed to fetch favorite")
        }

        mFavoriteChartUseVariant.getChart()?.onSuccess {
            mView?.hideFavoriteLoading()
            mView?.showFavoriteChart(it)
        }?.onError {
            logD("Failed to fetch favorite chart")
        }
    }

    private fun searchCurrency(currencyEntity: CurrencyEntity, body: ((index: Int) -> Unit)? = null): Int =
            mCachedData.findCurrency(currencyEntity, body)

    private fun updateCurrency(currencyEntity: CurrencyEntity): Int {
        val index = searchCurrency(currencyEntity)

        if (index >= 0) {
        } else {
            mCachedData.addSortedByRank(currencyEntity)
        }

        return 0
    }

    private fun removeCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData.removeAt(it)
            }

    private fun getCurrencies(skipCache: Boolean) = launchSilent(uiContext) {
        mView?.showLoading()
        mCurrencyListUseCases.getCryptoList(skipCache)
                .onSuccess {
                    mView?.hideLoading()
                    setCache(it)
                }
                .onError {
                    mView?.hideLoading()
                    when (it) {
                        is NetworkException -> {
                            mView?.showNetworkError(mCachedData.isEmpty())
                        }
                    }
                }
    }

    //endregion

    //region Contract

    override fun attachView(view: WatchListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCurrencyListUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun onResume() {
        super.onResume()
        mCurrencyListUseCases.addObserver(mCurrenciesObserver)
        getCurrencies(false)
    }

    override fun getCurrencyList() {
        getCurrencies(true)
    }

    override fun onCurrencyPick(position: Int) {
    }

    override fun deleteCurrency(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            if (mCachedData[position].isSaved) {
                mView?.showMessage("${mCachedData[position].name} removed from Watchlist")
                mCurrencyListUseCases.removeCurrency(mCachedData[position].id)
            }
        }
    }

    override fun onCurrencyClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            mView?.openCurrencyScreen(mCachedData[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}