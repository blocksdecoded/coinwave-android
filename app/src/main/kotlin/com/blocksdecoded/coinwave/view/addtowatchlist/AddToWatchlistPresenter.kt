package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AddToWatchlistPresenter(
    view: AddToWatchlistContract.View?,
    private val mCoinsUseCases: CoinsUseCases,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<AddToWatchlistContract.View>(view), AddToWatchlistContract.Presenter {

    private var cached = arrayListOf<CoinEntity>()

    override fun attachView(view: AddToWatchlistContract.View) {
        mView = view
        injectSelfToView()
    }

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCoins()
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CoinEntity>) {
        cached.clear()
        cached.addAll(coins)
        mView?.showCoins(coins)
    }

    private fun showError(e: Throwable) {
        mView?.showLoadingError()
    }

    private fun updateCurrencyWatch(position: Int) {
        if (cached.isValidIndex(position)) {
            cached[position].isSaved = !cached[position].isSaved

            if (cached[position].isSaved) {
                mCoinsUseCases.saveCoin(cached[position].id)
            } else {
                mCoinsUseCases.removeCoin(cached[position].id)
            }

            mView?.updateCoin(cached[position])
        }
    }

    //endregion

    //region Contract

    override fun getCoins() = launchSilent(uiContext) {
        mView?.hideLoadingError()
        mCoinsUseCases.getCoins(false)
                .onSuccess(::updateCache)
                .onError(::showError)
    }

    override fun onCoinClick(position: Int) {
        updateCurrencyWatch(position)
    }

    override fun onCoinWatch(position: Int) {
        updateCurrencyWatch(position)
    }

    //endregion
}