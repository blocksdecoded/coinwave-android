package com.blocksdecoded.coinwave.data.ipfs.model

import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 2/11/19
 * with Android Studio
 */
data class LatestRate (
        @SerializedName("rate") val value: Double,
        @SerializedName("date") val timestamp: Long
)