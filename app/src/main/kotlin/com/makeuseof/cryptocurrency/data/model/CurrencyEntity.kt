package com.makeuseof.cryptocurrency.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CurrencyEntity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("symbol") val symbol: String,
        @SerializedName("rank") val rank: Int,
        @SerializedName("total_supply") val totalSupply: Float,
        @SerializedName("circulating_supply") val circulatingSupply: Float,
        @SerializedName("max_supply") val maxSupply: Float?,
        @SerializedName("last_updated") val lastUpdated: Long,
        @SerializedName("quotes") val quotes: HashMap<String, Quote>
) {
    override fun toString(): String {
        return "$id $symbol $name"
    }
}