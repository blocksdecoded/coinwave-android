package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.Exceptions
import com.blocksdecoded.coinwave.data.coins.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.coins.ICoinsStorage
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.domain.usecases.chart.IChartsUseCases.ChartPeriod
import com.blocksdecoded.coinwave.domain.usecases.chart.IChartsUseCases.ChartPeriod.*
import io.reactivex.Single
import kotlin.collections.HashMap

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum as RequestChartPeriod

class ChartsInteractor(
    private val mCoinsStorage: ICoinsStorage,
    private val mChartsStorage: IChartsStorage
) : IChartsUseCases {
    private var mCachedId: Int = -1
    private var cachedChart: HashMap<String, ChartData> = HashMap()

    private fun getRequestPeriod(period: ChartPeriod): RequestChartPeriod {
        return when (period) {
            TODAY -> RequestChartPeriod.DAY
            WEEK -> RequestChartPeriod.WEEK
            MONTH_1 -> RequestChartPeriod.MONTH
            MONTH_3 -> RequestChartPeriod.MONTH
            MONTH_6 -> RequestChartPeriod.YEAR
            YEAR -> RequestChartPeriod.YEAR
            ALL -> RequestChartPeriod.FIVE_YEARS
        }
    }

    override fun getChartData(coinId: Int, period: ChartPeriod): Single<ChartData> {
        if (mCachedId != coinId) {
            mCachedId = coinId
            cachedChart.clear()
        }

        return if (cachedChart[period.toString()] == null) {
            val coin = mCoinsStorage.getCoin(coinId)

            if (coin != null) {
                mChartsStorage.getChart(coin.symbol, getRequestPeriod(period))
                    .doOnSuccess { cachedChart[period.toString()] = it }
            } else {
                Single.error(Exceptions.EmptyCache("Currency not found"))
            }
        } else {
            Single.just(cachedChart[period.toString()]!!)
        }
    }
}