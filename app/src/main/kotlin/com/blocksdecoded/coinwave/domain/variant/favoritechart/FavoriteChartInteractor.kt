package com.blocksdecoded.coinwave.domain.variant.favoritechart

import android.content.res.Resources
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.utils.coroutine.model.Result

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class FavoriteChartInteractor(
        private val mChartsUseCases: ChartsUseCases,
        private val mFavoriteUseCases: FavoriteUseCases,
        private val mCurrencyUseCases: CurrencyListUseCases
): FavoriteChartUseVariant {
    override suspend fun getChart(): Result<ChartData>? {
        return mChartsUseCases.getChartData(mFavoriteUseCases.getId(), CHART_PERIOD)
    }

    override suspend fun getCurrency(): Result<CurrencyEntity>? {
        val entity = mCurrencyUseCases.getCurrency(mFavoriteUseCases.getId())

        return if (entity != null) {
            Result.Success(entity)
        } else {
            Result.Error(Resources.NotFoundException())
        }
    }

    companion object {
        @JvmStatic
        private val CHART_PERIOD = ChartsUseCases.ChartPeriod.TODAY
    }
}