package com.makeuseof.cryptocurrency.view.list.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.util.FormatUtil
import com.makeuseof.cryptocurrency.util.format
import com.makeuseof.utils.ResourceUtil
import com.makeuseof.utils.hide
import com.makeuseof.utils.visible

// Created by askar on 7/19/18.
class CurrencyListViewHolder(
        view: View,
        private val mListener: CurrencyVHClickListener
): RecyclerView.ViewHolder(view) {
    private val mSymbol: TextView = itemView.findViewById(R.id.adapter_currency_symbol)
    private val mMarketCap: TextView = itemView.findViewById(R.id.adapter_currency_market_cap)
    private val mVolume: TextView = itemView.findViewById(R.id.adapter_currency_volume)
    private val mPrice: TextView = itemView.findViewById(R.id.adapter_currency_price)
    private val mPriceChange: TextView = itemView.findViewById(R.id.adapter_currency_price_change)
    private val mPriceChangeIcon: ImageView = itemView.findViewById(R.id.adapter_currency_price_change_icon)
    private val mTopDivider: View = itemView.findViewById(R.id.adapter_currency_top_divider)

    init {
        itemView.setOnClickListener { mListener.onClick(adapterPosition) }
    }

    fun onBind(currency: CurrencyEntity){
        mSymbol.text = "${currency.symbol}"
        mMarketCap.text = "$${FormatUtil.withSuffix(currency.getMarketCap()!!)}"
        mVolume.text = "$${FormatUtil.withSuffix(currency.getDailyVolume()!!)}"

        mPrice.text = "$${currency.getPrice()?.format()}"

        currency.getPriceChange()?.let {
            mPriceChange.text = "${if (it > 0) "+" else ""}$it%"

            val color: Int = if(it > 0f){
                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_up_green)
                ResourceUtil.getColor(itemView.context, R.color.green)
            } else {
                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_down_red)
                ResourceUtil.getColor(itemView.context, R.color.red)
            }

            mPrice.setTextColor(color)
            mPriceChange.setTextColor(color)
        }

        setupDividers()
    }

    private fun setupDividers(){
        when(adapterPosition){
            0 -> mTopDivider.visible()
            else -> mTopDivider.hide()
        }
    }

    interface CurrencyVHClickListener{
        fun onClick(position: Int)

        fun onPick(position: Int)
    }
}