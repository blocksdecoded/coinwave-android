package com.makeuseof.cryptocurrency.data.chart.network.model

import com.makeuseof.cryptocurrency.data.model.ChartDataEntry

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
data class ChartListResponse(
        val change: String,
        val history: List<ChartDataEntry>
)