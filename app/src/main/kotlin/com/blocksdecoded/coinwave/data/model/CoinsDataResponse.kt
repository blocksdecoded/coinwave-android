package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 1/29/19
 * with Android Studio
 */
data class CoinsDataResponse(
    @Expose @SerializedName("coins") var coins: List<CoinEntity>
)