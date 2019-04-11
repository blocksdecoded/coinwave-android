package com.blocksdecoded.coinwave.presentation.editwatchlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.presentation.editwatchlist.recycler.EditWatchlistAdapter
import com.blocksdecoded.coinwave.presentation.editwatchlist.recycler.EditWatchlistVH
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.visible
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class EditWatchlistFragment : BaseMvpFragment<EditWatchlistContract.Presenter>(),
        EditWatchlistContract.View,
        EditWatchlistVH.WatchlistVHClickListener {

    override val presenter: EditWatchlistContract.Presenter by inject { parametersOf(this@EditWatchlistFragment) }
    override val layoutId: Int = R.layout.fragment_edit_watchlist

    private var mAdapter: EditWatchlistAdapter? = null

    @BindView(R.id.connection_error_container)
    lateinit var mErrorContainer: View
    @BindView(R.id.edit_watchlist_recycler)
    lateinit var mRecycler: RecyclerView

    @OnClick(
            R.id.back,
            R.id.connection_error_retry
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.back -> finishView()

            R.id.connection_error_retry -> presenter.getCoins()
        }
    }

    override fun initView(rootView: View) {
        mAdapter = EditWatchlistAdapter(listener = this)

        mRecycler.setHasFixedSize(true)
        mRecycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        mRecycler.adapter = mAdapter
    }

    //region ViewHolder

    override fun onClick(position: Int) {
        presenter.onCoinClick(position)
    }

    override fun onWatchClick(position: Int) {
        presenter.onCoinWatch(position)
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
        fun newInstance() = EditWatchlistFragment()
    }
}