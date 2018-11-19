package com.makeuseof.cryptocurrency.data.watchlist

import com.makeuseof.utils.shared.SharedContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

// Created by askar on 7/20/18.
class WatchlistService(
        private val mSharedStorage: SharedContract
): WatchlistSourceContract {
    private var mInitialized = false
    private val SAVED_CURRENCIES = "saved_currencies"
    private val saveDelimiter = ","

    private var mSavedCache = arrayListOf<Int>()

    companion object {
        private var INSTANCE: WatchlistService? = null

        fun getInstance(sharedStorage: SharedContract): WatchlistSourceContract{
            if(INSTANCE == null){
                INSTANCE = WatchlistService(sharedStorage)
            }
            return INSTANCE!!
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }

    //region Private

    private fun fetchData(){
        if(!mInitialized){
            mInitialized = true
            mSavedCache = load()
            mSharedStorage.getPreference(SAVED_CURRENCIES, hashSetOf<String>())
        }
    }

    private fun save(ids: ArrayList<Int>) = GlobalScope.async {
        var save = ""

        ids.forEachIndexed { index, i ->
            save += "$i${if (index == ids.size - 1) "" else saveDelimiter}"
        }

        mSharedStorage.setPreference(SAVED_CURRENCIES, save)
    }

    private fun load(): ArrayList<Int> = try {
        val saved = mSharedStorage.getPreference(SAVED_CURRENCIES, "")
        val result = arrayListOf<Int>()
        result.addAll(saved.split(saveDelimiter).map { it.toInt() })
        result
    } catch (e: Exception) {
        arrayListOf()
    }

    //endregion

    //region Contract
    override fun addId(id: Int): Boolean {
        mSavedCache.add(id)
        save(mSavedCache)
        return true
    }

    override fun deleteId(id: Int): Boolean {
        mSavedCache.remove(id)
        save(mSavedCache)
        return true
    }

    override fun getAll(): List<Int> {
        fetchData()
        return mSavedCache
    }

    //endregion
}