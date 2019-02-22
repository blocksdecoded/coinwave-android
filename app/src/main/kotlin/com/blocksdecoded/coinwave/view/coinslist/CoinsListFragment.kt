package com.blocksdecoded.coinwave.view.coinslist

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
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.view.coininfo.CoinInfoActivity
import com.blocksdecoded.coinwave.view.coinslist.recycler.CoinsListAdapter
import com.blocksdecoded.coinwave.view.coinslist.recycler.CoinsListVH
import com.blocksdecoded.coinwave.view.widgets.ActionConfirmDialog
import com.blocksdecoded.utils.*

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

    @OnClick(R.id.coin_menu)
    fun onClick(view: View) {
        when (view.id) {
            R.id.coin_menu -> mPresenter?.onMenuClick()
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

    override fun showDeleteConfirm(coinEntity: CoinEntity, position: Int) {
        activity?.let {
            mActiveDialog = ActionConfirmDialog(it)
                    .setCancelListener { it.dismiss() }
                    .setTitle("Remove ${coinEntity.name} from Watchlist?")
                    .setConfirmText("Remove")
                    .setConfirmListener {
                        it.dismiss()
                        mPresenter?.deleteCoin(position)
                    }.setDismissListener {
                        mActiveDialog = null
                    }.showDialog()
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
        ): CoinsListFragment = CoinsListFragment().apply {
            arguments = Bundle()
            arguments?.putString(TITLE_KEY, title)
        }

        fun getTitle(arguments: Bundle?): String {
            return arguments?.getString(TITLE_KEY) ?: ""
        }
    }
}