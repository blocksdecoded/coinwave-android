package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CurrencyEntity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("symbol") val symbol: String,
        @SerializedName("iconUrl") val iconUrl: String,
        @SerializedName("websiteUrl") val websiteSlug: String,
        @SerializedName("rank") val rank: Int,
        @SerializedName("totalSupply") val totalSupply: Float,
        @SerializedName("circulatingSupply") val circulatingSupply: Float,
        @SerializedName("volume") val volume: Long,
        @SerializedName("marketCap") val marketCap: Long,
        @SerializedName("price") val price: Float,
        @SerializedName("change") val priceChange: Float,
        var isSaved: Boolean = false
) {
    override fun toString(): String {
        return "$id $symbol $name"
    }

    fun getMarketCap(): Float? = marketCap.toFloat()
    fun getDailyVolume(): Float? = volume.toFloat()
    fun getPrice(): Float? = price
    fun getPriceChange(): Float? = priceChange
}