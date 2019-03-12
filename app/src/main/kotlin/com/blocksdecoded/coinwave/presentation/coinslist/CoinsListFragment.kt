package com.blocksdecoded.coinwave.presentation.coinslist

import android.app.Dialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoActivity
import com.blocksdecoded.coinwave.presentation.coinslist.recycler.CoinsListAdapter
import com.blocksdecoded.coinwave.presentation.coinslist.recycler.CoinsListVH
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.coinwave.presentation.widgets.CoinsHeaderView
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.statusBarHeight
import com.blocksdecoded.utils.extensions.visible
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

open class CoinsListFragment : BaseMvpFragment<CoinsListContract.Presenter>(),
    CoinsListContract.View,
    CoinsListVH.CoinVHListener {
    override val presenter: CoinsListContract.Presenter by inject { parametersOf(this@CoinsListFragment, context) }
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

    private var mAdapter: CoinsListAdapter? = null
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

        mAdapter = CoinsListAdapter(arrayListOf(), this)

        mRetry.setOnClickListener {
            presenter.getCoins()
        }

        mSwipeRefreshLayout.setOnRefreshListener {
            presenter.getCoins()
        }

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.layoutManager = lm
        mRecycler.adapter = mAdapter
        mRecycler.setHasFixedSize(true)
    }

    //endregion

    //region ViewHolder

    override fun onClick(position: Int) {
        presenter.onCoinClick(position)
    }

    //endregion

    //region Contract

    override fun showSortType(sortType: CoinsCache.CoinSortEnum) {
        mListHeader.currentState = sortType
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
        mRecycler.visible()
        mListHeader.visible()
        mErrorContainer.hide()
        mRecycler.post { mAdapter?.setItems(coins) }
    }

    override fun showNetworkError(hideList: Boolean) {
        if (hideList) {
            mListHeader.hide()
            mRecycler.hide()
            mErrorContainer.visible()
        } else {
            showShortToast(context, getString(R.string.message_connection_error))
            mListHeader.visible()
            mRecycler.visible()
            mErrorContainer.hide()
        }
    }

    override fun hideLoading() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading() {
        mSwipeRefreshLayout.isRefreshing = true
        mErrorContainer.hide()
        mRecycler.hide()
        mListHeader.hide()
    }

    //endregion

    companion object {
        fun newInstance() = CoinsListFragment()
    }
}