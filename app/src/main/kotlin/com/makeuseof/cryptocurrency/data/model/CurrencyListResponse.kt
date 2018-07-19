package com.makeuseof.cryptocurrency.data.model

// Created by askar on 7/19/18.
data class CurrencyListResponse(
        val data: List<CurrencyEntity>,
        val metadata: CurrencyMetadata
)