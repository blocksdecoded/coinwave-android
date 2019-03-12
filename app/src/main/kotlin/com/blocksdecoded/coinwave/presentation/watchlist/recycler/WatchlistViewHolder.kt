package com.blocksdecoded.coinwave.presentation.watchlist.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.util.FormatUtil
import com.blocksdecoded.coinwave.util.format
import com.blocksdecoded.coinwave.util.loadIcon
import com.blocksdecoded.utils.extensions.getColorRes
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.visible

// Created by askar on 7/19/18.
class WatchlistViewHolder(
    view: View,
    private val mListener: CurrencyVHClickListener
) : RecyclerView.ViewHolder(view) {
    private val mSymbolIcon: ImageView = itemView.findViewById(R.id.adapter_coin_icon)
    private val mSymbol: TextView = itemView.findViewById(R.id.adapter_coin_symbol)
    private val mMarketCap: TextView = itemView.findViewById(R.id.adapter_coin_market_cap)
    private val mVolume: TextView = itemView.findViewById(R.id.adapter_coin_volume)
    private val mPrice: TextView = itemView.findViewById(R.id.adapter_coin_price)
    private val mPriceChange: TextView = itemView.findViewById(R.id.adapter_coin_price_change)
    private val mPriceChangeIcon: ImageView = itemView.findViewById(R.id.adapter_coin_price_change_icon)
    private val mTopDivider: View = itemView.findViewById(R.id.adapter_coin_top_divider)

    init {
        itemView.setOnClickListener { mListener.onClick(adapterPosition) }
    }

    fun onBind(coin: CoinEntity) {
        mSymbol.text = "${coin.symbol}"
        mMarketCap.text = "$${FormatUtil.withSuffix(coin.getMarketCap()!!)}"
        mVolume.text = "$${FormatUtil.withSuffix(coin.getDailyVolume()!!)}"

        mPrice.text = "$${coin.getPrice()?.format()}"

        configPriceChanges(coin)

        setupDividers()
        mSymbolIcon.loadIcon(coin)
    }

    private fun configPriceChanges(coin: CoinEntity) {
        coin.getPriceChange()?.let {
            mPriceChange.text = "${if (it > 0) "+" else ""}$it%"

            val color: Int = if (it > 0f) {
                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_up_green)
                itemView.getColorRes(R.color.green)
            } else {
                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_down_red)
                itemView.getColorRes(R.color.red)
            }

            mPrice.setTextColor(color)
            mPriceChange.setTextColor(color)
        }
    }

    private fun setupDividers() {
        when (adapterPosition) {
            0 -> mTopDivider.visible()
            else -> mTopDivider.hide()
        }
    }

    interface CurrencyVHClickListener {
        fun onClick(position: Int)

        fun onPick(position: Int)
    }
}