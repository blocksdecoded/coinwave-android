package com.blocksdecoded.coinwave.data.coins.remote.model

import com.blocksdecoded.coinwave.data.model.ChartDataEntry
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
data class ChartListResponse(
    @Expose @SerializedName("change") val change: Float,
    @Expose @SerializedName("history") val history: List<ChartDataEntry>
)