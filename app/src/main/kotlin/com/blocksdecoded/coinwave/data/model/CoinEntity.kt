package com.blocksdecoded.coinwave.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CoinEntity(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("symbol") val symbol: String,
    @Expose @SerializedName("iconUrl") val iconUrl: String,
    @Expose @SerializedName("websiteUrl") val websiteSlug: String,
    @Expose @SerializedName("rank") val rank: Int,
    @Expose @SerializedName("totalSupply") val totalSupply: Float,
    @Expose @SerializedName("circulatingSupply") val circulatingSupply: Float,
    @Expose @SerializedName("volume") val volume: Long,
    @Expose @SerializedName("marketCap") val marketCap: Long,
    @Expose @SerializedName("price") val price: Float,
    @Expose @SerializedName("change") val priceChange: Float,
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