package com.blocksdecoded.coinwave.presentation.pickfavorite

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.favorite.IFavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpPresenter
import com.blocksdecoded.utils.rx.uiSubscribe

class PickFavoritePresenter(
    override var view: IPickFavoriteContract.View?,
    private val mFavoriteUseCases: IFavoriteUseCases,
    private val mCoinsUseCases: ICoinsUseCases
) : BaseMvpPresenter<IPickFavoriteContract.View>(), IPickFavoriteContract.Presenter {

    private val mCoinsCache = CoinsCache()

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCoins()
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CoinEntity>) {
        mCoinsCache.setCache(coins)
        refreshView()
    }

    private fun refreshView() {
        view?.showCoins(mCoinsCache.coins)
        view?.showSortType(mCoinsCache.currentSort)
    }

    private fun getCoins() {
        view?.hideError()
        view?.showLoading()
        mCoinsUseCases.getCoins(false)
                .uiSubscribe(
                        onNext = {
                            view?.hideLoading()
                            updateCache(it)
                        },
                        onError = {
                            view?.hideLoading()
                            view?.showError()
                        }
                )
    }

    //endregion

    //region Contract

    override fun onCoinClick(position: Int) {
        if (mCoinsCache.isValidIndex(position)) {
            mFavoriteUseCases.setId(mCoinsCache.coins[position].id)
            view?.showMessage("Favorite saved.")
            view?.finishView()
        }
    }

    override fun onRetryClick() {
        getCoins()
    }

    override fun onSortClick(sortType: ViewSortEnum) {
        mCoinsCache.updateSortType(sortType)
        refreshView()
    }

    //endregion
}