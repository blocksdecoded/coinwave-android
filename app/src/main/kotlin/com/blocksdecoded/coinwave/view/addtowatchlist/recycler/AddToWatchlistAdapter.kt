package com.blocksdecoded.coinwave.view.addtowatchlist.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.utils.inflate

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
class AddToWatchlistAdapter(
        private val coins: ArrayList<CurrencyEntity> = arrayListOf(),
        private val listener: AddToWatchlistVH.WatchlistVHClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            AddToWatchlistVH(parent.inflate(R.layout.adapter_watchlist_item)!!, listener)

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddToWatchlistVH -> holder.onBind(coins[position])
        }
    }

    fun setCoins(coins: List<CurrencyEntity>) {
        this.coins.clear()
        this.coins.addAll(coins)
        notifyDataSetChanged()
    }
}