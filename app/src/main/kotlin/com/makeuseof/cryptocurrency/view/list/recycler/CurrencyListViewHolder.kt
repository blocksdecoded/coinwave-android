package com.makeuseof.cryptocurrency.view.list.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

// Created by askar on 7/19/18.
class CurrencyListViewHolder(
        view: View,
        private val mListener: CurrencyVHClickListener
): RecyclerView.ViewHolder(view) {
    private val mSymbol: TextView = itemView.findViewById(R.id.adapter_currency_symbol)
    private val mMarketCap: TextView = itemView.findViewById(R.id.adapter_currency_market_cap)
    private val mVolume: TextView = itemView.findViewById(R.id.adapter_currency_volume)
    private val mPrice: TextView = itemView.findViewById(R.id.adapter_currency_price)

    init {
        itemView.setOnClickListener { mListener.onClick(adapterPosition) }
    }

    fun onBind(currency: CurrencyEntity){
        mSymbol.text = currency.symbol
        mMarketCap.text = currency.totalSupply.toString()
        mVolume.text = currency.quotes.dailyVolume.toString()
        mPrice.text = currency.quotes.price.toString()
    }

    interface CurrencyVHClickListener{
        fun onClick(position: Int)

        fun onPick(position: Int)
    }
}