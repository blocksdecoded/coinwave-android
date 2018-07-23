package com.makeuseof.cryptocurrency.view.watchlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistAdapter
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistViewHolder
import com.makeuseof.utils.hide
import com.makeuseof.utils.inflate
import com.makeuseof.utils.visible

class WatchListFragment :
        BaseMVPFragment<WatchListContract.Presenter>(),
        WatchListContract.View,
        WatchlistViewHolder.CurrencyVHClickListener {
    override var mPresenter: WatchListContract.Presenter? = null

    private var mProgress: ProgressBar? = null
    private var mRecycler: RecyclerView? = null
    private var mAdapter: WatchlistAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_watchlist)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?) {
        mAdapter = WatchlistAdapter(arrayListOf(), this)
        mRecycler = rootView?.findViewById(R.id.fragment_watchlist_recycler)
        mProgress = rootView?.findViewById(R.id.fragment_watchlist_progress)

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.layoutManager = lm
        mRecycler?.adapter = mAdapter
    }

    //region ViewHolder

    override fun onPick(position: Int) {
        mPresenter?.onCurrencyPick(position)
    }

    override fun onClick(position: Int) {
        mPresenter?.onCurrencyClick(position)
    }

    //endregion

    //region Contract

    override fun updateCurrency(currency: CurrencyEntity) {
        mAdapter?.updateItem(currency)
    }

    override fun deleteCurrency(currency: CurrencyEntity) {
        mAdapter?.deleteItem(currency)
    }

    override fun showCurrencies(currencies: List<CurrencyEntity>) {
        mProgress.hide()
        mRecycler.visible()
        mAdapter?.setItems(currencies)
    }

    override fun showNetworkError() {

    }

    override fun showLoading() {
        mProgress.visible()
        mRecycler.hide()
    }

    //endregion
}