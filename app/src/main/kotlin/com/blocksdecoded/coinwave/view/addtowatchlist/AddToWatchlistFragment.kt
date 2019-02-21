package com.blocksdecoded.coinwave.view.addtowatchlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.view.addtowatchlist.recycler.AddToWatchlistAdapter
import com.blocksdecoded.coinwave.view.addtowatchlist.recycler.AddToWatchlistVH
import com.blocksdecoded.core.mvp.BaseMVPFragment

class AddToWatchlistFragment : BaseMVPFragment<AddToWatchlistContract.Presenter>(),
        AddToWatchlistContract.View,
        AddToWatchlistVH.WatchlistVHClickListener {
    override val layoutId: Int = R.layout.fragment_add_to_watchlist
    override var mPresenter: AddToWatchlistContract.Presenter? = null

    private var mAdapter: AddToWatchlistAdapter? = null

    @BindView(R.id.add_to_watchlist_recycler)
    lateinit var mRecycler: RecyclerView

    @OnClick(R.id.back)
    fun onClick(view: View) {
        when (view.id) {
            R.id.back -> finishView()
        }
    }

    override fun initView(rootView: View) {
        mAdapter = AddToWatchlistAdapter(listener = this)

        mRecycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        mRecycler.adapter = mAdapter
    }

    //region ViewHolder

    override fun onClick(position: Int) {
        mPresenter?.onCurrencyClick(position)
    }

    override fun onWatchClick(position: Int) {
        mPresenter?.onCurrencyWatch(position)
    }

    //endregion

    //region Contract

    override fun showCurrencies(currencies: List<CurrencyEntity>) {
        mAdapter?.setCoins(currencies)
    }

    override fun updateCurrency(currencyEntity: CurrencyEntity) {
        mAdapter?.updateItem(currencyEntity)
    }

    //endregion

    companion object {
        fun newInstance() = AddToWatchlistFragment()
    }
}