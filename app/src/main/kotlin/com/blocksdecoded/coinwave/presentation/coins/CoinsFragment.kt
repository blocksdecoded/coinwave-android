package com.blocksdecoded.coinwave.presentation.coins

import android.app.Dialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoActivity
import com.blocksdecoded.coinwave.presentation.coins.recycler.CoinsAdapter
import com.blocksdecoded.coinwave.presentation.coins.recycler.CoinViewHolder
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.widgets.CoinsHeaderView
import com.blocksdecoded.coinwave.util.DateHelper
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.extensions.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

open class CoinsFragment : BaseMvpFragment<ICoinsContract.Presenter>(),
    ICoinsContract.View,
    CoinViewHolder.CoinVHListener {
    override val presenter: ICoinsContract.Presenter by inject { parametersOf(this@CoinsFragment, context) }
    override val layoutId: Int = R.layout.fragment_coins_list

    @BindView(R.id.fragment_coin_list_recycler)
    lateinit var mRecycler: RecyclerView
    @BindView(R.id.fragment_coin_list_refresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.fragment_coin_list_error)
    lateinit var mErrorContainer: View
    @BindView(R.id.connection_error_retry)
    lateinit var mRetry: View
    @BindView(R.id.fragment_coin_list_header)
    lateinit var mListHeader: CoinsHeaderView
    @BindView(R.id.fragment_coins_progress)
    lateinit var mLoadingProgress: View
    @BindView(R.id.fragment_coins_last_updated)
    lateinit var mLastUpdated: TextView

    private var mAdapter: CoinsAdapter? = null
    private var mActiveDialog: Dialog? = null

    @OnClick(
            R.id.coin_menu
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.coin_menu -> presenter.onMenuClick()
        }
    }

    //region Lifecycle

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        mListHeader.setListener {
            presenter.onSortClick(it)
        }

        context?.also {
            rootView.setPadding(0, it.statusBarHeight, 0, 0)
        }

        mAdapter = CoinsAdapter(arrayListOf(), this)

        mRetry.setOnClickListener {
            presenter.getCoins()
        }

        mSwipeRefreshLayout.setOnRefreshListener {
            presenter.getCoins()
        }

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.setHasFixedSize(true)
        mRecycler.layoutManager = lm
        mRecycler.adapter = mAdapter
    }

    //endregion

    //region ViewHolder

    override fun onClick(position: Int) {
        presenter.onCoinClick(position)
    }

    //endregion

    //region Contract

    override fun showSortType(sortType: CoinsCache.CoinSortEnum) {
        mListHeader.currentSort = sortType
    }

    override fun openCoinInfo(id: Int) {
        activity?.let {
            CoinInfoActivity.start(it, id)
        }
    }

    override fun updateCoin(position: Int, coinEntity: CoinEntity) {
        mAdapter?.updateItem(coinEntity)
    }

    override fun deleteCoin(position: Int) {
        mAdapter?.deleteItem(position)
    }

    override fun showCoins(coins: List<CoinEntity>) {
        mErrorContainer.hide()
        mAdapter?.setItems(coins)
        mRecycler.post { mRecycler.scrollToPosition(0) }
    }

    override fun showNetworkError(hideList: Boolean) {
        if (hideList) {
            hideList()
            mErrorContainer.visible()
        } else {
            showShortToast(context, getString(R.string.message_connection_error))
        }
    }

    override fun hideLoading() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading() {
        mSwipeRefreshLayout.isRefreshing = true
        mErrorContainer.hide()
    }

    override fun showProgress() {
        mLoadingProgress.visible()
    }

    override fun hideProgress() {
        mLoadingProgress.invisible()
    }

    override fun showLastUpdated(date: Date) {
        mLastUpdated.setColouredSpanAfter(
                getString(R.string.last_updated, DateHelper.getRelativeDate(date)),
                ":",
                mLastUpdated.context.getColorRes(R.color.green))
    }

    override fun hideLastUpdated() {
    }

    override fun hideList() {
        mRecycler.invisible()
        mListHeader.invisible()
    }

    override fun showList() {
        mRecycler.visible()
        mListHeader.visible()
    }

    //endregion

    companion object {
        fun newInstance() = CoinsFragment()
    }
}