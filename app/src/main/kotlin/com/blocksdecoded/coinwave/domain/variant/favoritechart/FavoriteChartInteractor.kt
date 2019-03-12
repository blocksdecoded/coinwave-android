package com.blocksdecoded.coinwave.domain.variant.favoritechart

import android.content.res.Resources
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.chart.IChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.favorite.IFavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import com.blocksdecoded.utils.coroutine.model.Result
import io.reactivex.Flowable

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class FavoriteChartInteractor(
        private val mChartsUseCases: IChartsUseCases,
        private val mFavoriteUseCases: IFavoriteUseCases,
        private val mCoinsUseCases: ICoinsUseCases
) : IFavoriteChartUseVariant {
    override val chart: Flowable<ChartData>?
        get() = mChartsUseCases.getChartData(mFavoriteUseCases.getId(), CHART_PERIOD).toFlowable()

    override suspend fun getCoin(): Result<CoinEntity>? {
        val entity = mCoinsUseCases.getCoin(mFavoriteUseCases.getId())

        return if (entity != null) {
            Result.Success(entity)
        } else {
            Result.Error(Resources.NotFoundException())
        }
    }

    companion object {
        @JvmStatic
        private val CHART_PERIOD = IChartsUseCases.ChartPeriod.TODAY
    }
}