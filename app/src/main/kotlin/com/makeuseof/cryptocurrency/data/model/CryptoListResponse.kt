package com.makeuseof.cryptocurrency.data.model

// Created by askar on 7/19/18.
data class CryptoListResponse(
        val data: List<CryptoEntity>,
        val metadata: CryptoMetadata
)