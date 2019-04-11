package com.blocksdecoded.coinwave.domain.variant.favoritechart

import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.chart.IChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.favorite.IFavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import io.reactivex.Observable

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class FavoriteChartInteractor(
    private val mChartsUseCases: IChartsUseCases,
    private val mFavoriteUseCases: IFavoriteUseCases,
    private val mCoinsUseCases: ICoinsUseCases
) : IFavoriteChartUseVariant {
    override val chart: Observable<ChartData>?
        get() = mChartsUseCases.getChartData(mFavoriteUseCases.getId(), CHART_PERIOD).toObservable()

    override fun getCoin(): CoinEntity? = mCoinsUseCases.getCoin(mFavoriteUseCases.getId())

    companion object {
        private val CHART_PERIOD = IChartsUseCases.ChartPeriod.TODAY
    }
}