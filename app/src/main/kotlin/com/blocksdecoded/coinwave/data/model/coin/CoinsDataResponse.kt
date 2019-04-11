package com.blocksdecoded.coinwave.data.model.coin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by askar on 1/29/19
 * with Android Studio
 */
data class CoinsDataResponse(
    @Expose @SerializedName("coins") var coins: List<CoinEntity>,
    @Expose @SerializedName("updated_at") var updatedAt: Date? = Date()
)