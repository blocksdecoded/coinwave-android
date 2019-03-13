package com.blocksdecoded.coinwave.presentation.coinslist.recycler

import androidx.recyclerview.widget.DiffUtil
import com.blocksdecoded.coinwave.data.model.CoinEntity

class CoinDiffUtil(
        private val oldCoins: List<CoinEntity>,
        private val newCoins: List<CoinEntity>
): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldCoins[oldItemPosition].id == newCoins[newItemPosition].id

    override fun getOldListSize(): Int = oldCoins.size

    override fun getNewListSize(): Int = newCoins.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCoin = oldCoins[oldItemPosition]
        val newCoin = newCoins[oldItemPosition]

        return oldCoin.id == newCoin.id && oldCoin.price == newCoin.price
                && oldCoin.priceChange == newCoin.priceChange
                && oldCoin.marketCap == newCoin.marketCap
    }
}