package com.makeuseof.cryptocurrency.view.watchlist.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.util.addSortedByRank
import com.makeuseof.utils.Lg
import com.makeuseof.utils.inflate
import com.makeuseof.utils.isValidIndex

// Created by askar on 7/19/18.
class WatchlistAdapter(
        private var mItems: ArrayList<CurrencyEntity>,
        private val mListener: WatchlistViewHolder.CurrencyVHClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WatchlistViewHolder(parent.inflate(R.layout.adapter_currency_item)!!, mListener)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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