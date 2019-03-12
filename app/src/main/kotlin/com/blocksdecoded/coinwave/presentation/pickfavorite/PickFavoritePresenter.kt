package com.blocksdecoded.coinwave.presentation.pickfavorite

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.extensions.isValidIndex
import com.blocksdecoded.utils.rx.uiSubscribe

class PickFavoritePresenter(
    override var view: PickFavoriteContract.View?,
    private val mFavoriteUseCases: FavoriteUseCases,
    private val mCoinsUseCases: CoinsUseCases
) : BaseMvpPresenter<PickFavoriteContract.View>(), PickFavoriteContract.Presenter {

    private var mCachedData = arrayListOf<CoinEntity>()

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCoins()
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CoinEntity>) {
        mCachedData.clear()
        mCachedData.addAll(coins)
        view?.showCoins(mCachedData)
    }

    private fun getCoins() {
        view?.hideError()
        view?.showLoading()
        mCoinsUseCases.getCoins(false)
                .uiSubscribe(
                        onNext = { updateCache(it) },
                        onError = { view?.showError() },
                        onComplete = { view?.hideLoading() }
                )
    }

    //endregion

    //region Contract

    override fun onCoinClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            mFavoriteUseCases.setId(mCachedData[position].id)
            view?.showMessage("Favorite saved.")
            view?.finishView()
        }
    }

    override fun onRetryClick() {
        getCoins()
    }

    //endregion
}