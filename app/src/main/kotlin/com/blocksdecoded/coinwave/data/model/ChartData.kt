package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// Created by askar on 7/25/18.
data class ChartData(
    @Expose @SerializedName("chart") val chart: List<ChartDataEntry>
)