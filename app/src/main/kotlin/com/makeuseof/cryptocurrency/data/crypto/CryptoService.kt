package com.makeuseof.cryptocurrency.data.crypto

// Created by askar on 7/19/18.
class CryptoService: CryptoSourceContract {
    companion object {
        private var INSTANCE: CryptoService? = null

        fun getInstance(): CryptoSourceContract{
            if (INSTANCE == null){
                INSTANCE = CryptoService()
            }
            return INSTANCE!!
        }
    }

}