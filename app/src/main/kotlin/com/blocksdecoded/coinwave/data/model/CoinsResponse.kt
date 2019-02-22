package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CoinsResponse(
    @SerializedName("data") val data: CoinsDataResponse,
    @SerializedName("status") val status: String
)