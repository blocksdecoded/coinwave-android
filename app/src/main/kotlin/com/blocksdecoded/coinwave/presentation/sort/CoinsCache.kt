package com.blocksdecoded.coinwave.presentation.sort

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache.CoinSortEnum.*
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.extensions.isValidIndex

class CoinsCache {
    val coins = ArrayList<CoinEntity>()

    var currentSort = MARKET_CAP_DES
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
            sortType == CAP && currentSort != MARKET_CAP_DES -> MARKET_CAP_DES
            sortType == CAP && currentSort == MARKET_CAP_DES -> MARKET_CAP_ASC
            sortType == PRICE && currentSort != PRICE_DES -> PRICE_DES
            sortType == PRICE && currentSort == PRICE_DES -> PRICE_ASC
            sortType == VOLUME && currentSort != VOLUME_DES -> VOLUME_DES
            sortType == VOLUME && currentSort == VOLUME_DES -> VOLUME_ASC

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

    fun remove(coinEntity: CoinEntity): Int = coins.findCurrency(coinEntity) {
        coins.removeAt(it)
    }

    //endregion

    enum class CoinSortEnum(
        var sort: (coins: ArrayList<CoinEntity>) -> Unit
    ) {
        DEFAULT({ it.sortBy { it.rank } }),
        NAME_ASC({ it.sortByDescending { it.symbol } }),
        NAME_DES({ it.sortBy { it.symbol } }),
        MARKET_CAP_ASC({ it.sortBy { it.marketCap } }),
        MARKET_CAP_DES({ it.sortByDescending { it.marketCap } }),
        VOLUME_ASC({ it.sortBy { it.volume } }),
        VOLUME_DES({ it.sortByDescending { it.volume } }),
        PRICE_ASC({ it.sortBy { it.priceChange } }),
        PRICE_DES({ it.sortByDescending { it.priceChange } })
    }
}