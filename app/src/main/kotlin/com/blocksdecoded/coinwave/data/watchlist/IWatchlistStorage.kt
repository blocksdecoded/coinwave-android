package com.blocksdecoded.coinwave.data.watchlist

// Created by askar on 7/20/18.
interface IWatchlistStorage {
    fun addId(id: Int): Boolean

    fun deleteId(id: Int): Boolean

    fun getAll(): List<Int>
}