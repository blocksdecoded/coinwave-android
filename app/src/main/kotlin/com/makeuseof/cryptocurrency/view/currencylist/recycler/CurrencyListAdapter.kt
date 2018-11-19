package com.makeuseof.cryptocurrency.view.currencylist.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.util.findCurrency
import com.makeuseof.utils.inflate
import com.makeuseof.utils.isValidIndex

// Created by askar on 7/19/18.
class CurrencyListAdapter(
        private var mItems: ArrayList<CurrencyEntity>,
        private val mListener: CurrencyListViewHolder.CurrencyVHClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CurrencyListViewHolder(parent.inflate(R.layout.adapter_currency_item)!!, mListener)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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