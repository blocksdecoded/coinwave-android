package com.makeuseof.cryptocurrency.view.watchlist

import android.app.Dialog
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.view.currency.CurrencyActivity
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistAdapter
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistViewHolder
import com.makeuseof.cryptocurrency.view.widgets.ActionConfirmDialog
import com.makeuseof.cryptocurrency.view.widgets.RecyclerItemSpaceDecoration
import com.makeuseof.utils.*

open class WatchListFragment :
        BaseMVPFragment<WatchListContract.Presenter>(),
        WatchListContract.View,
        WatchlistViewHolder.CurrencyVHClickListener {

    companion object {
        fun newInstance(): WatchListFragment = WatchListFragment()
    }

    override var mPresenter: WatchListContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_watchlist

    @BindView(R.id.fragment_watchlist_recycler)
    @JvmField var mRecycler: RecyclerView? = null
    private var mAdapter: WatchlistAdapter? = null

    @BindView(R.id.fragment_watchlist_refresh)
    @JvmField var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    @BindView(R.id.fragment_watchlist_empty)
    @JvmField var mEmptyText: View? = null

    @BindView(R.id.fragment_watchlist_error)
    @JvmField var mErrorContainer: View? = null
    @BindView(R.id.connection_error_retry)
    @JvmField var mRetry: View? = null

    private var mActiveDialog: Dialog? = null

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        context?.also {
            rootView.setPadding(0, DimenUtils.getStatusBarHeight(it), 0, 0)
        }
        mAdapter = WatchlistAdapter(arrayListOf(), this)

        mRetry?.setOnClickListener {
            mPresenter?.getCurrencyList()
        }

        mSwipeRefreshLayout?.setOnRefreshListener {
            mPresenter?.getCurrencyList()
        }

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.layoutManager = lm
        mRecycler?.adapter = mAdapter

        context?.let {
//            mRecycler?.addItemDecoration(RecyclerItemSpaceDecoration(mBottomSpace = DimenUtils.dpToPx(it, 100)))
        }
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