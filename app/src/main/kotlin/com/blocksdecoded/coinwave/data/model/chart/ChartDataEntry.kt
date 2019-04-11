package com.blocksdecoded.coinwave.data.model.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChartDataEntry(
    @Expose @SerializedName("price") val price: String,
    @Expose @SerializedName("timestamp") val time: Long
)