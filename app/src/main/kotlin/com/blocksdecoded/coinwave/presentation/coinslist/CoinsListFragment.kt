package com.blocksdecoded.coinwave.presentation.coinslist

import android.app.Dialog
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.mvp.deprecated.BaseMVPFragment
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoActivity
import com.blocksdecoded.coinwave.presentation.coinslist.recycler.CoinsListAdapter
import com.blocksdecoded.coinwave.presentation.coinslist.recycler.CoinsListVH
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.statusBarHeight
import com.blocksdecoded.utils.extensions.visible

open class CoinsListFragment :
        BaseMVPFragment<CoinsListContract.Presenter>(),
        CoinsListContract.View,
        CoinsListVH.CoinVHListener {
    override var mPresenter: CoinsListContract.Presenter? = null
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
    lateinit var mListHeader: View

    @BindView(R.id.fragment_coin_list_title)
    lateinit var mTitle: TextView

    private var mAdapter: CoinsListAdapter? = null
    private var mActiveDialog: Dialog? = null

    @OnClick(
            R.id.coin_menu,
            R.id.coins_header_name,
            R.id.coins_header_market_cap,
            R.id.coins_header_price,
            R.id.coins_header_volume
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.coin_menu -> mPresenter?.onMenuClick()
            R.id.coins_header_name -> mPresenter?.onSortClick(NAME)
            R.id.coins_header_market_cap -> mPresenter?.onSortClick(CAP)
            R.id.coins_header_price -> mPresenter?.onSortClick(PRICE)
            R.id.coins_header_volume -> mPresenter?.onSortClick(VOLUME)
        }
    }

    //region Lifecycle

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        context?.also {
            rootView.setPadding(0, it.statusBarHeight, 0, 0)
        }

        mTitle?.text = getTitle(arguments)

        mAdapter = CoinsListAdapter(arrayListOf(), this)

        mRetry?.setOnClickListener {
            mPresenter?.getCoins()
        }

        mSwipeRefreshLayout?.setOnRefreshListener {
            mPresenter?.getCoins()
        }

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.layoutManager = lm
        mRecycler?.adapter = mAdapter
        mRecycler.setHasFixedSize(true)
    }

    //endregion

    //region ViewHolder

    override fun onPick(position: Int) {
        mPresenter?.onCoinPick(position)
    }

    override fun onClick(position: Int) {
        mPresenter?.onCoinClick(position)
    }

    //endregion

    //region Contract

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
        mRecycler?.post {
            mAdapter?.setItems(coins)
        }
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
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun showLoading() {
        mSwipeRefreshLayout?.isRefreshing = true
        mErrorContainer.hide()
        mRecycler.hide()
        mListHeader.hide()
    }

    //endregion

    companion object {
        private val TITLE_KEY = "list_title"

        fun newInstance(
            title: String
        ): CoinsListFragment = CoinsListFragment().apply {
            arguments = Bundle()
            arguments?.putString(TITLE_KEY, title)
        }

        fun getTitle(arguments: Bundle?): String {
            return arguments?.getString(TITLE_KEY) ?: ""
        }
    }
}