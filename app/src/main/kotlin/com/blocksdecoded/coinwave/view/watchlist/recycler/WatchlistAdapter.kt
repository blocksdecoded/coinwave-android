package com.blocksdecoded.coinwave.view.watchlist.recycler

import android.view.ViewGroup
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.util.addSortedByRank
import com.blocksdecoded.utils.inflate
import com.blocksdecoded.utils.isValidIndex

// Created by askar on 7/19/18.
class WatchlistAdapter(
        private var mItems: ArrayList<CurrencyEntity>,
        private val mListener: WatchlistViewHolder.CurrencyVHClickListener
): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return WatchlistViewHolder(parent.inflate(R.layout.adapter_currency_item)!!, mListener)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is WatchlistViewHolder -> holder.onBind(mItems[position])
        }
    }

    private fun findItem(id: Int, onFind: (index: Int) -> Unit){
        onFind.invoke(mItems.indexOfFirst { it.id == id })
    }

    fun setItems(currencies: List<CurrencyEntity>){
        mItems.clear()
        mItems.addAll(currencies)
        notifyDataSetChanged()
    }

    fun deleteItemAt(position: Int){
        if(mItems.isValidIndex(position)){
            mItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(currency: CurrencyEntity){
        findItem(currency.id) {
            deleteItemAt(it)
        }
    }

    fun updateItem(currency: CurrencyEntity){
        findItem(currency.id) {
            if(it >= 0){
                mItems[it] = currency
                notifyItemChanged(it)
            } else {
                mItems.addSortedByRank(currency)
                notifyDataSetChanged()
            }
        }
    }
}