package com.makeuseof.cryptocurrency.data.model

// Created by askar on 7/19/18.
data class CryptoEntity(
        val id: Int,
        val name: String,
        val symbol: String,
        val rank: Int,
        val totalSupply: Double,
        val circulatingSupply: Double,
        val maxSupply: Double?,
        val lastUpdated: Long,
        val quotes: Quotes
) {
}