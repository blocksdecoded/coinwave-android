package com.blocksdecoded.coinwave.presentation.sort

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache.CoinSortEnum.*
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.extensions.isValidIndex

class CoinsCache {
    val coins = ArrayList<CoinEntity>()

    var currentSort = PRICE_DES
        set(value) {
            field = value
            sortCoins()
        }

    private fun sortCoins() = currentSort.sort(coins)

    //region Public

    fun setCache(coins: Collection<CoinEntity>) {
        this.coins.clear()
        this.coins.addAll(coins)
        sortCoins()
    }

    fun updateSortType(sortType: ViewSortEnum) {
        currentSort = when {
            sortType == NAME && currentSort != NAME_DES -> NAME_DES
            sortType == NAME && currentSort == NAME_DES -> NAME_ASC
            sortType == CAP && currentSort != CAP_DES -> CAP_DES
            sortType == CAP && currentSort == CAP_DES -> CAP_ASC
            sortType == PRICE && currentSort != PRICE_DES -> PRICE_DES
            sortType == PRICE && currentSort == PRICE_DES -> PRICE_ASC
            sortType == VOLUME && currentSort != VOL_DES -> VOL_DES
            sortType == VOLUME && currentSort == VOL_DES -> VOL_ASC

            else -> DEFAULT
        }
    }

    fun updateCurrency(coinEntity: CoinEntity) = coins.findCurrency(coinEntity) {
        coins[it] = coinEntity
    }

    fun isEmpty(): Boolean = coins.isEmpty()

    fun isValidIndex(position: Int): Boolean = coins.isValidIndex(position)

    fun add(coinEntity: CoinEntity): Int = coins.findCurrency(coinEntity) {
        if (it == -1) {
            coins.add(coinEntity)
            sortCoins()
            coins.addSortedByRank(coinEntity)
        }
    }

    fun remove(coinEntity: CoinEntity) : Int = coins.findCurrency(coinEntity) {
        coins.removeAt(it)
    }

    //endregion

    enum class CoinSortEnum(
            var sort: (coins: ArrayList<CoinEntity>) -> Unit
    ) {
        DEFAULT({ it.sortBy { it.rank } }),
        NAME_ASC({ it.sortByDescending { it.symbol } }),
        NAME_DES({ it.sortBy { it.symbol } }),
        CAP_ASC({ it.sortBy { it.marketCap } }),
        CAP_DES({ it.sortByDescending { it.marketCap } }),
        VOL_ASC({ it.sortBy { it.volume } }),
        VOL_DES({ it.sortByDescending { it.volume } }),
        PRICE_ASC({ it.sortBy { it.price } }),
        PRICE_DES({ it.sortByDescending { it.price } })
    }
}