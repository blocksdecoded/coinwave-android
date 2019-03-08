package com.blocksdecoded.coinwave.presentation.addtowatchlist.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.utils.extensions.inflate

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
class AddToWatchlistAdapter(
    private val coins: ArrayList<CoinEntity> = arrayListOf(),
    private val listener: AddToWatchlistVH.WatchlistVHClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            AddToWatchlistVH(parent.inflate(R.layout.item_watchlist)!!, listener)

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddToWatchlistVH -> holder.onBind(coins[position])
        }
    }

    fun setCoins(coins: List<CoinEntity>) {
        this.coins.clear()
        this.coins.addAll(coins)
        notifyDataSetChanged()
    }

    private fun findItem(id: Int, onFind: (index: Int) -> Unit) {
        onFind.invoke(coins.indexOfFirst { it.id == id })
    }

    fun updateItem(coin: CoinEntity) {
        findItem(coin.id) {
            if (it >= 0) {
                coins[it] = coin
                notifyItemChanged(it)
            } else {
                coins.addSortedByRank(coin)
                notifyDataSetChanged()
            }
        }
    }
}