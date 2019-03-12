package com.blocksdecoded.coinwave.presentation.addtowatchlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.addtowatchlist.recycler.AddToWatchlistAdapter
import com.blocksdecoded.coinwave.presentation.addtowatchlist.recycler.AddToWatchlistVH
import com.blocksdecoded.core.mvp.deprecated.BaseMVPFragment
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.visible

class AddToWatchlistFragment : BaseMVPFragment<AddToWatchlistContract.Presenter>(),
        AddToWatchlistContract.View,
        AddToWatchlistVH.WatchlistVHClickListener {
    override val layoutId: Int = R.layout.fragment_add_to_watchlist
    override var mPresenter: AddToWatchlistContract.Presenter? = null

    private var mAdapter: AddToWatchlistAdapter? = null

    @BindView(R.id.connection_error_container)
    lateinit var mErrorContainer: View
    @BindView(R.id.add_to_watchlist_recycler)
    lateinit var mRecycler: RecyclerView

    @OnClick(
            R.id.back,
            R.id.connection_error_retry
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.back -> finishView()

            R.id.connection_error_retry -> mPresenter?.getCoins()
        }
    }

    override fun initView(rootView: View) {
        mAdapter = AddToWatchlistAdapter(listener = this)

        mRecycler.setHasFixedSize(true)
        mRecycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        mRecycler.adapter = mAdapter
    }

    //region ViewHolder

    override fun onClick(position: Int) {
        mPresenter?.onCoinClick(position)
    }

    override fun onWatchClick(position: Int) {
        mPresenter?.onCoinWatch(position)
    }

    //endregion

    //region Contract

    override fun showCoins(coins: List<CoinEntity>) {
        mRecycler.visible()
        mAdapter?.setCoins(coins)
    }

    override fun updateCoin(coinEntity: CoinEntity) {
        mAdapter?.updateItem(coinEntity)
    }

    override fun hideLoadingError() {
        mErrorContainer.hide()
    }

    override fun showLoadingError() {
        mRecycler.hide()
        mErrorContainer.visible()
    }

    //endregion

    companion object {
        fun newInstance() = AddToWatchlistFragment()
    }
}