package com.makeuseof.cryptocurrency.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CurrencyListResponse(
        @SerializedName("data") val data: CurrencyDataResponse,
        @SerializedName("status") val status: String
)