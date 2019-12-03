package com.blocksdecoded.coinwave.data.model.coin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// Created by askar on 7/19/18.
data class CoinEntity(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String = "",
    @Expose @SerializedName("symbol") val symbol: String = "",
    @Expose @SerializedName("iconUrl") var iconUrl: String = "",
    @Expose @SerializedName("websiteUrl") val websiteSlug: String = "",
    @Expose @SerializedName("rank") val rank: Int = 1,
    @Expose @SerializedName("totalSupply") val totalSupply: Float = 0f,
    @Expose @SerializedName("circulatingSupply") val circulatingSupply: Float = 0f,
    @Expose @SerializedName("volume") val volume: Float = 0f,
    @Expose @SerializedName("marketCap") val marketCap: Float = 0f,
    @Expose @SerializedName("price") val price: Double = 0.0,
    @Expose @SerializedName("change") val priceChange: Float = 0f,
    var isSaved: Boolean = false
) {
    override fun toString(): String {
        return "$id $symbol $name"
    }

    fun getMarketCap(): Float? = marketCap
    fun getDailyVolume(): Float? = volume
    fun getPrice(): Double? = price
    fun getPriceChange(): Float? = priceChange
}