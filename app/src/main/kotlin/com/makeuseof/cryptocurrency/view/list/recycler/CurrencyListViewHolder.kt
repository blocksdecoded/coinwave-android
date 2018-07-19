package com.makeuseof.cryptocurrency.view.list.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
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
    private val mTopDivider: View = itemView.findViewById(R.id.adapter_currency_top_divider)

    init {
        itemView.setOnClickListener { mListener.onClick(adapterPosition) }
    }

    fun onBind(currency: CurrencyEntity){
        mSymbol.text = currency.symbol
        mMarketCap.text = currency.totalSupply.toString()
        mVolume.text = currency.quotes["USD"]?.dailyVolume.toString()
        mPrice.text = "$${currency.quotes["USD"]?.price}"

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