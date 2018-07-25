package com.makeuseof.cryptocurrency.view.watchlist

import android.app.Dialog
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.view.currency.CurrencyActivity
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistAdapter
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistViewHolder
import com.makeuseof.cryptocurrency.view.widgets.ActionConfirmDialog
import com.makeuseof.utils.hide
import com.makeuseof.utils.inflate
import com.makeuseof.utils.showShortToast
import com.makeuseof.utils.visible

class WatchListFragment :
        BaseMVPFragment<WatchListContract.Presenter>(),
        WatchListContract.View,
        WatchlistViewHolder.CurrencyVHClickListener {
    override var mPresenter: WatchListContract.Presenter? = null

    private var mRecycler: RecyclerView? = null
    private var mAdapter: WatchlistAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mEmptyText: View? = null

    private var mErrorContainer: View? = null
    private var mRetry: View? = null

    private var mActiveDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_watchlist)

        initView(rootView)

        return rootView
    }

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    private fun initView(rootView: View?) {
        mAdapter = WatchlistAdapter(arrayListOf(), this)
        mRecycler = rootView?.findViewById(R.id.fragment_watchlist_recycler)
        mSwipeRefreshLayout = rootView?.findViewById(R.id.fragment_watchlist_refresh)
        mEmptyText = rootView?.findViewById(R.id.fragment_watchlist_empty)

        mErrorContainer = rootView?.findViewById(R.id.fragment_watchlist_error)
        mRetry = rootView?.findViewById(R.id.connection_error_retry)

        mRetry?.setOnClickListener {
            mPresenter?.getCurrencyList()
        }

        mSwipeRefreshLayout?.setOnRefreshListener {
            mPresenter?.getCurrencyList()
        }

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

    override fun openCurrencyScreen(id: Int) {
        activity?.let {
            CurrencyActivity.start(it, id)
        }
    }

    override fun showDeleteConfirm(currencyEntity: CurrencyEntity, position: Int) {
        activity?.let {
            mActiveDialog = ActionConfirmDialog(it)
                    .setCancelListener { it.dismiss() }
                    .setTitle("Remove ${currencyEntity.name} from Watchlist?")
                    .setConfirmText("Remove")
                    .setConfirmListener {
                        it.dismiss()
                        mPresenter?.deleteCurrency(position)
                    }.setDismissListener {
                        mActiveDialog = null
                    }.showDialog()
        }
    }

    override fun updateCurrency(position: Int, currencyEntity: CurrencyEntity) {
        mRecycler.visible()
        mEmptyText.hide()
        mAdapter?.updateItem(currencyEntity)
    }

    override fun deleteCurrency(position: Int) {
        mRecycler.visible()
        mAdapter?.deleteItemAt(position)
    }

    override fun showCurrencies(currencies: List<CurrencyEntity>) {
        mSwipeRefreshLayout?.isRefreshing = false
        mRecycler.visible()
        mErrorContainer.hide()
        mEmptyText.hide()
        mRecycler?.post {
            mAdapter?.setItems(currencies)
        }
    }

    override fun showEmpty() {
        mSwipeRefreshLayout?.isRefreshing = false
        mErrorContainer.hide()
        mEmptyText.visible()
        mRecycler?.hide()
    }

    override fun showNetworkError(hideList: Boolean) {
        mSwipeRefreshLayout?.isRefreshing = false

        if(hideList){
            mErrorContainer.visible()
            mRecycler.hide()
        } else {
            showShortToast(context, "Can't refresh currencies.\nPlease check internet connection and try again.")
            mErrorContainer.hide()
            mRecycler.visible()
        }
    }

    override fun showLoading() {
        mSwipeRefreshLayout?.isRefreshing = true
        mEmptyText.hide()
        mErrorContainer.hide()
    }

    //endregion
}