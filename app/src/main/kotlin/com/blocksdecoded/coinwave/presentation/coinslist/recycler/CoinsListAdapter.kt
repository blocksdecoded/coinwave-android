package com.blocksdecoded.coinwave.presentation.coinslist.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.extensions.inflate
import com.blocksdecoded.utils.extensions.isValidIndex

// Created by askar on 7/19/18.
class CoinsListAdapter(
    private var mCoins: ArrayList<CoinEntity>,
    private val mListener: CoinsListVH.CoinVHListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoinsListVH(parent.inflate(R.layout.item_coin)!!, mListener)
    }

    override fun getItemCount(): Int = mCoins.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CoinsListVH -> holder.onBind(mCoins[position])
        }
    }

    fun setItems(coins: List<CoinEntity>) {
        mCoins.clear()
        mCoins.addAll(coins)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        if (mCoins.isValidIndex(position)) {
            mCoins.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(coin: CoinEntity) {
        mCoins.findCurrency(coin) {
            mCoins[it] = coin
            notifyItemChanged(it)
        }
    }
}