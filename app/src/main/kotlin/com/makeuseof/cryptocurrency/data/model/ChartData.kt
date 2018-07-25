package com.makeuseof.cryptocurrency.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/25/18.
data class ChartData(
        @SerializedName("price_usd") val usdChart: List<List<Long>>
) {
}