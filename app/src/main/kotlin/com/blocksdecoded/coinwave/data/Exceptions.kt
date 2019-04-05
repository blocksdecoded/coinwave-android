package com.blocksdecoded.coinwave.data

// Created by askar on 7/19/18.
sealed class Exceptions {
    class NetworkException(message: String) : Exception(message)
    class EmptyCache(message: String) : Exception(message)
    class EmptyLocalCache : Exception()
}