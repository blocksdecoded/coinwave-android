package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class Quote(
        @SerializedName("price") val price: Float,
        @SerializedName("volume_24h") val dailyVolume: Float,
        @SerializedName("market_cap") val marketCap: Float,
        @SerializedName("percent_change_1h") val hourChange: Float,
        @SerializedName("percent_change_24h") val dayChange: Float,
        @SerializedName("percent_change_7d") val weekChange: Float
)