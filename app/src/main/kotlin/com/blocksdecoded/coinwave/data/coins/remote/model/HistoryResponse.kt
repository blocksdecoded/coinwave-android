package com.blocksdecoded.coinwave.data.coins.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
data class HistoryResponse(
    @Expose @SerializedName("status") val status: String,
    @Expose @SerializedName("data") val data: ChartListResponse
)