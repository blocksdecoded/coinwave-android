package com.blocksdecoded.coinwave.data.model.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChartData(
    @Expose @SerializedName("chart") val chart: List<ChartDataEntry>
)