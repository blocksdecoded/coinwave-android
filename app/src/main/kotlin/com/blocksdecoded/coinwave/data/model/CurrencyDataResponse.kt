package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 1/29/19
 * with Android Studio
 */
data class CurrencyDataResponse(
        @SerializedName("coins") val coins: List<CurrencyEntity>
)