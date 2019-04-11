package com.blocksdecoded.coinwave.domain.variant.favoritechart

import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import io.reactivex.Observable

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
interface IFavoriteChartUseVariant {
    val chart: Observable<ChartData>?

    fun getCoin(): CoinEntity?
}