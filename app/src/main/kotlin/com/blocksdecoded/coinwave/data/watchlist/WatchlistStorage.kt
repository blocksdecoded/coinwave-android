package com.blocksdecoded.coinwave.data.watchlist

import com.blocksdecoded.utils.shared.ISharedStorage

// Created by askar on 7/20/18.
class WatchlistStorage(
    private val mSharedStorage: ISharedStorage
) : IWatchlistStorage {
    private var mInitialized = false
    private val SAVED_COINS = "saved_coins"
    private val saveDelimiter = ","

    private var mSavedCache = arrayListOf<Int>()

    //region Private

    private fun fetchData() {
        if (!mInitialized) {
            mInitialized = true
            mSavedCache = load()
            mSharedStorage.getPreference(SAVED_COINS, hashSetOf<String>())
        }
    }

    private fun save(ids: ArrayList<Int>) {
        var save = ""

        ids.forEachIndexed { index, i ->
            save += "$i${if (index == ids.size - 1) "" else saveDelimiter}"
        }

        mSharedStorage.setPreference(SAVED_COINS, save)
    }

    private fun load(): ArrayList<Int> = try {
        val saved = mSharedStorage.getPreference(SAVED_COINS, "")
        val result = arrayListOf<Int>()
        result.addAll(saved.split(saveDelimiter).map { it.toInt() })
        result
    } catch (e: Exception) {
        arrayListOf()
    }

    //endregion

    //region Contract
    override fun addId(id: Int): Boolean = if (!mSavedCache.contains(id)) {
        mSavedCache.add(id)
        save(mSavedCache)
        true
    } else {
        false
    }

    override fun deleteId(id: Int): Boolean = if (mSavedCache.contains(id)) {
        mSavedCache.remove(id)
        save(mSavedCache)
        true
    } else {
        false
    }

    override fun getAll(): List<Int> {
        fetchData()
        return mSavedCache
    }

    //endregion
}