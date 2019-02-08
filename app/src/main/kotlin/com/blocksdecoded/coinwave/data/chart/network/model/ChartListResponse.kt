package com.blocksdecoded.coinwave.data.chart.network.model

import com.blocksdecoded.coinwave.data.model.ChartDataEntry

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
data class ChartListResponse(
        val change: String,
        val history: List<ChartDataEntry>
)