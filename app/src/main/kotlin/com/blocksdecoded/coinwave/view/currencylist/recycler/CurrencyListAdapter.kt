package com.blocksdecoded.coinwave.view.currencylist.recycler

import android.view.ViewGroup
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.utils.inflate
import com.blocksdecoded.utils.isValidIndex

// Created by askar on 7/19/18.
class CurrencyListAdapter(
        private var mItems: ArrayList<CurrencyEntity>,
        private val mListener: CurrencyListViewHolder.CurrencyVHClickListener
): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return CurrencyListViewHolder(parent.inflate(R.layout.adapter_currency_item)!!, mListener)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CurrencyListViewHolder -> holder.onBind(mItems[position])
        }
    }

    fun setItems(currencies: List<CurrencyEntity>){
        mItems.clear()
        mItems.addAll(currencies)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        if(mItems.isValidIndex(position)){
            mItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(currency: CurrencyEntity){
        mItems.findCurrency(currency) {
            mItems[it] = currency
            notifyItemChanged(it)
        }
    }
}