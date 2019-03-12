package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.EmptyCache
import com.blocksdecoded.coinwave.data.crypto.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import io.reactivex.Single
import kotlin.collections.HashMap

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum as RequestChartPeriod


class ChartsInteractor(
    private val mCryptoService: ICoinsStorage,
    private val mChartsService: IChartsStorage
) : ChartsUseCases {
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
            val coin = mCryptoService.getCoin(coinId)

            if (coin != null) {
                mChartsService.getChart(coin.symbol, getRequestPeriod(period))
                    .doOnSuccess { cachedChart[period.toString()] = it }
            } else {
                Single.error(EmptyCache("Currency not found"))
            }
        } else {
            Single.just(cachedChart[period.toString()]!!)
        }
    }
}