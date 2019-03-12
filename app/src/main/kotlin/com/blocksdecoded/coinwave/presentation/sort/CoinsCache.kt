package com.blocksdecoded.coinwave.presentation.sort

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache.CoinSortEnum.*
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.extensions.isValidIndex

class CoinsCache {
    val cachedCoins = ArrayList<CoinEntity>()

    enum class CoinSortEnum {
        DEFAULT,
        NAME_ASC,
        NAME_DES,
        CAP_ASC,
        CAP_DES,
        VOL_ASC,
        VOL_DES,
        PRICE_ASC,
        PRICE_DES
    }

    var currentSort = DEFAULT

    fun setCache(coins: Collection<CoinEntity>) {
        cachedCoins.clear()
        cachedCoins.addAll(coins)
        sortCoins()
    }

    fun updateSortType(sortType: ViewSortEnum) {
        currentSort = when {
            sortType == NAME && currentSort != NAME_ASC -> NAME_ASC
            sortType == NAME && currentSort == NAME_ASC -> NAME_DES
            sortType == CAP && currentSort != CAP_DES -> CAP_DES
            sortType == CAP && currentSort == CAP_DES -> CAP_ASC
            sortType == PRICE && currentSort != PRICE_DES -> PRICE_DES
            sortType == PRICE && currentSort == PRICE_DES -> PRICE_ASC
            sortType == VOLUME && currentSort != VOL_DES -> VOL_DES
            sortType == VOLUME && currentSort == VOL_DES -> VOL_ASC

            else -> DEFAULT
        }

        sortCoins()
    }

    private fun sortCoins() = when (currentSort) {
        DEFAULT -> cachedCoins.sortBy { it.rank }
        NAME_ASC -> cachedCoins.sortBy { it.symbol }
        NAME_DES -> cachedCoins.sortByDescending { it.symbol }
        CAP_ASC -> cachedCoins.sortBy { it.marketCap }
        CAP_DES -> cachedCoins.sortByDescending { it.marketCap }
        VOL_ASC -> cachedCoins.sortBy { it.volume }
        VOL_DES -> cachedCoins.sortByDescending { it.volume }
        PRICE_ASC -> cachedCoins.sortBy { it.price }
        PRICE_DES -> cachedCoins.sortByDescending { it.price }
    }

    fun updateCurrency(coinEntity: CoinEntity) = cachedCoins.findCurrency(coinEntity) {
        cachedCoins[it] = coinEntity
    }

    fun isEmpty(): Boolean = cachedCoins.isEmpty()

    fun isValidIndex(position: Int): Boolean = cachedCoins.isValidIndex(position)
}