package com.makeuseof.cryptocurrency.domain

import com.makeuseof.cryptocurrency.data.crypto.CryptoService
import com.makeuseof.cryptocurrency.data.crypto.CryptoSourceContract

// Created by askar on 7/19/18.
object ServiceProvider {
    fun getCurrencyService(): CryptoSourceContract = CryptoService.getInstance()
}