package com.blocksdecoded.coinwave.view.addtowatchlist.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
class AddToWatchlistVH(
        view: View,
        private val mListener: WatchlistVHClickListener
): RecyclerView.ViewHolder(view) {

    private val nameTextView: TextView = itemView.findViewById(R.id.adapter_watchlist_item_name)
    private val symbolTextView: TextView = itemView.findViewById(R.id.adapter_watchlist_item_symbol)
    private val mWatchIcon: ImageView = itemView.findViewById(R.id.adapter_watchlist_item_star)

    init {
        itemView.setOnClickListener { mListener.onClick(adapterPosition) }
    }

    fun onBind(coin: CurrencyEntity) {
        nameTextView.text = coin.name
        symbolTextView.text = coin.symbol

        mWatchIcon.setImageResource(if (coin.isSaved) R.drawable.ic_star_filled else R.drawable.ic_star_border)
    }

    interface WatchlistVHClickListener {
        fun onClick(position: Int)

        fun onWatchClick(position: Int)
    }
}