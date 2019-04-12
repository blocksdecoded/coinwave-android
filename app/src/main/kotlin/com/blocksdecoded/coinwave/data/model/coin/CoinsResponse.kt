package com.blocksdecoded.coinwave.data.model.coin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CoinsResponse(
    @Expose @SerializedName("status") val status: String,
    @Expose @SerializedName("data") val data: CoinsDataResponse
)