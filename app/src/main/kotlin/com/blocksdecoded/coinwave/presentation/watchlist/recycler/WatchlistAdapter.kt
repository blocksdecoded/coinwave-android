package com.blocksdecoded.coinwave.presentation.watchlist.recycler

import android.view.ViewGroup
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.utils.inflate
import com.blocksdecoded.utils.isValidIndex

// Created by askar on 7/19/18.
class WatchlistAdapter(
    private var mCoins: ArrayList<CoinEntity>,
    private val mListener: WatchlistViewHolder.CurrencyVHClickListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return WatchlistViewHolder(parent.inflate(R.layout.item_coin)!!, mListener)
    }

    override fun getItemCount(): Int = mCoins.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WatchlistViewHolder -> holder.onBind(mCoins[position])
        }
    }

    private fun findItem(id: Int, onFind: (index: Int) -> Unit) {
        onFind.invoke(mCoins.indexOfFirst { it.id == id })
    }

    fun setItems(coins: List<CoinEntity>) {
        mCoins.clear()
        mCoins.addAll(coins)
        notifyDataSetChanged()
    }

    fun deleteItemAt(position: Int) {
        if (mCoins.isValidIndex(position)) {
            mCoins.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(coin: CoinEntity) {
        findItem(coin.id) {
            deleteItemAt(it)
        }
    }

    fun updateItem(coin: CoinEntity) {
        findItem(coin.id) {
            if (it >= 0) {
                mCoins[it] = coin
                notifyItemChanged(it)
            } else {
                mCoins.addSortedByRank(coin)
                notifyDataSetChanged()
            }
        }
    }
}