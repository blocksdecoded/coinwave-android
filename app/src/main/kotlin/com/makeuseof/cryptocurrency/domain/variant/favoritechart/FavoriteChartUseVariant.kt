package com.makeuseof.cryptocurrency.domain.variant.favoritechart

import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.utils.coroutine.model.Result

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
interface FavoriteChartUseVariant {
    suspend fun getChart(): Result<ChartData>?
}