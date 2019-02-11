package com.blocksdecoded.coinwave.data.ipfs.model

import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 2/11/19
 * with Android Studio
 */
data class CurrencyInfo (
        @SerializedName("code") val code: String,
        @SerializedName("type") val type: String,
        @SerializedName("code_numeric") val position: Long,
        @SerializedName("display_name") val name: String
)