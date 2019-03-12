package com.blocksdecoded.coinwave.presentation.sort

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache.CoinSortEnum.*
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.extensions.isValidIndex

class CoinsCache {
    val cachedCoins = ArrayList<CoinEntity>()

    enum class CoinSortEnum(
            var sort: (coins: ArrayList<CoinEntity>) -> Unit
    ) {
        DEFAULT({ }),
        NAME_ASC({ it.sortBy { it.symbol } }),
        NAME_DES({ it.sortByDescending { it.symbol } }),
        CAP_ASC({ it.sortBy { it.marketCap } }),
        CAP_DES({ it.sortByDescending { it.marketCap } }),
        VOL_ASC({ it.sortBy { it.volume } }),
        VOL_DES({ it.sortByDescending { it.volume } }),
        PRICE_ASC({ it.sortBy { it.price } }),
        PRICE_DES({ it.sortByDescending { it.price } })
    }

    private var currentSort = DEFAULT
        set(value) {
            field = value
            sortCoins()
        }

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
    }

    private fun sortCoins() = currentSort.sort(cachedCoins)

    fun updateCurrency(coinEntity: CoinEntity) = cachedCoins.findCurrency(coinEntity) {
        cachedCoins[it] = coinEntity
    }

    fun isEmpty(): Boolean = cachedCoins.isEmpty()

    fun isValidIndex(position: Int): Boolean = cachedCoins.isValidIndex(position)
}