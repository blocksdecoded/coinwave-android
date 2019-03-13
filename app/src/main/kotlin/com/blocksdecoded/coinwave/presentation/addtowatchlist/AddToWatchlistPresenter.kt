package com.blocksdecoded.coinwave.presentation.addtowatchlist

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.extensions.isValidIndex
import com.blocksdecoded.utils.rx.uiSubscribe

class AddToWatchlistPresenter(
    override var view: IAddToWatchlistContract.View?,
    private val mCoinsUseCases: ICoinsUseCases
) : BaseMvpPresenter<IAddToWatchlistContract.View>(), IAddToWatchlistContract.Presenter {

    private var cached = arrayListOf<CoinEntity>()

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCoins()
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CoinEntity>) = launchSilent(scope) {
        cached.clear()
        cached.addAll(coins)
        view?.showCoins(coins)
    }

    private fun showError(e: Throwable) {
        view?.showLoadingError()
    }

    private fun updateCurrencyWatch(position: Int) {
        if (cached.isValidIndex(position)) {
            cached[position].isSaved = !cached[position].isSaved

            if (cached[position].isSaved) {
                mCoinsUseCases.saveCoin(cached[position].id)
            } else {
                mCoinsUseCases.removeCoin(cached[position].id)
            }

            view?.updateCoin(cached[position])
        }
    }

    //endregion

    //region Contract

    override fun getCoins() {
        view?.hideLoadingError()
        mCoinsUseCases.getCoins(false)
            .uiSubscribe(
                    onNext = { updateCache(it) },
                    onError = { showError(it) },
                    onComplete = {}
            )
    }

    override fun onCoinClick(position: Int) {
        updateCurrencyWatch(position)
    }

    override fun onCoinWatch(position: Int) {
        updateCurrencyWatch(position)
    }

    //endregion
}