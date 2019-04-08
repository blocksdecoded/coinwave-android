package com.blocksdecoded.coinwave.presentation.watchlist.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.coins.recycler.CoinDiffUtil
import com.blocksdecoded.utils.extensions.inflate

// Created by askar on 7/19/18.
class WatchlistAdapter(
    private var mCoins: ArrayList<CoinEntity>,
    private val mListener: WatchlistViewHolder.CurrencyVHClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WatchlistViewHolder(parent.inflate(R.layout.item_coin)!!, mListener)
    }

    override fun getItemCount(): Int = mCoins.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WatchlistViewHolder -> holder.onBind(mCoins[position])
        }
    }

    fun setItems(coins: List<CoinEntity>) {
        val diffResult = DiffUtil.calculateDiff(CoinDiffUtil(mCoins, coins))
        mCoins.clear()
        mCoins.addAll(coins)
        diffResult.dispatchUpdatesTo(this)
    }
}