package com.blocksdecoded.coinwave.domain.variant.favoritechart

import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.utils.coroutine.model.Result

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
interface FavoriteChartUseVariant {
    suspend fun getChart(): Result<ChartData>?

    suspend fun getCurrency(): Result<CoinEntity>?
}