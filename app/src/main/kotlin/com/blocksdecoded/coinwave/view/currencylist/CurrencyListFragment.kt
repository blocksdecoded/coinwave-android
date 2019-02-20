package com.blocksdecoded.coinwave.view.currencylist

import android.app.Dialog
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.mvp.BaseMVPFragment
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.view.currency.CurrencyActivity
import com.blocksdecoded.coinwave.view.currencylist.recycler.CurrencyListAdapter
import com.blocksdecoded.coinwave.view.currencylist.recycler.CurrencyListViewHolder
import com.blocksdecoded.coinwave.view.widgets.ActionConfirmDialog
import com.blocksdecoded.utils.*

open class CurrencyListFragment :
        BaseMVPFragment<CurrencyListContract.Presenter>(),
        CurrencyListContract.View,
        CurrencyListViewHolder.CurrencyVHClickListener {
    override var mPresenter: CurrencyListContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_currency_list

    @BindView(R.id.fragment_currency_list_recycler)
    lateinit var mRecycler: RecyclerView
    @BindView(R.id.fragment_currency_list_refresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.fragment_currency_list_error)
    lateinit var mErrorContainer: View
    @BindView(R.id.connection_error_retry)
    lateinit var mRetry: View
    @BindView(R.id.fragment_currency_list_header)
    lateinit var mListHeader: View

    @BindView(R.id.fragment_currency_list_title)
    lateinit var mTitle: TextView

    private var mAdapter: CurrencyListAdapter? = null
    private var mActiveDialog: Dialog? = null

    @OnClick(R.id.currency_menu)
    fun onClick(view: View) {
        when (view.id) {
            R.id.currency_menu -> mPresenter?.onMenuClick()
        }
    }

    //region Lifecycle

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        context?.also {
            rootView.setPadding(0, DimenUtils.getStatusBarHeight(it), 0, 0)
        }

        mTitle?.text = getTitle(arguments)

        mAdapter = CurrencyListAdapter(arrayListOf(), this)

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

    //endregion

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
        mAdapter?.updateItem(currencyEntity)
    }

    override fun deleteCurrency(position: Int) {
        mAdapter?.deleteItem(position)
    }

    override fun showCurrencies(currencies: List<CurrencyEntity>) {
        mRecycler.visible()
        mListHeader.visible()
        mErrorContainer.hide()
        mRecycler?.post {
            mAdapter?.setItems(currencies)
        }
    }

    override fun showNetworkError(hideList: Boolean) {
        if(hideList){
            mListHeader.hide()
            mRecycler.hide()
            mErrorContainer.visible()
        } else {
            showShortToast(context, "Can't refresh currencies.\nPlease check internet connection and try again.")
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
        ): CurrencyListFragment = CurrencyListFragment().apply {
            arguments = Bundle()
            arguments?.putString(TITLE_KEY, title)
        }

        fun getTitle(arguments: Bundle?): String{
            return arguments?.getString(TITLE_KEY)?:""
        }
    }
}