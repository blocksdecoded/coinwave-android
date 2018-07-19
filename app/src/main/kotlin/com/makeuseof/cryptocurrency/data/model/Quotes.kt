package com.makeuseof.cryptocurrency.data.model

// Created by askar on 7/19/18.
data class Quotes(
        val price: Double,
        val dailyVolume: Double,
        val marketCap: Double,
        val hourChange: Double,
        val dayChange: Double,
        val weekChange: Double
)