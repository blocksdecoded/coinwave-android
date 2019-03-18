package com.blocksdecoded.coinwave.domain.variant.favoritechart

import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.utils.coroutine.model.Result
import io.reactivex.Observable

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
interface IFavoriteChartUseVariant {
    val chart: Observable<ChartData>?

    suspend fun getCoin(): Result<CoinEntity>?
}