package com.blocksdecoded.coinwave.presentation.watchlist

import android.os.Handler
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.util.format
import com.blocksdecoded.coinwave.util.init
import com.blocksdecoded.coinwave.util.loadChartData
import com.blocksdecoded.coinwave.presentation.addtowatchlist.AddToWatchlistActivity
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoActivity
import com.blocksdecoded.coinwave.presentation.pickfavorite.PickFavoriteActivity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.watchlist.recycler.WatchlistAdapter
import com.blocksdecoded.coinwave.presentation.watchlist.recycler.WatchlistViewHolder
import com.blocksdecoded.coinwave.presentation.widgets.CoinsHeaderView
import com.blocksdecoded.coinwave.presentation.widgets.chart.ChartListener
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.extensions.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

open class WatchListFragment : BaseMvpFragment<IWatchListContract.Presenter>(),
    IWatchListContract.View,
    WatchlistViewHolder.CurrencyVHClickListener {

    override val presenter: IWatchListContract.Presenter by inject { parametersOf(this@WatchListFragment, context) }
    override val layoutId: Int = R.layout.fragment_watchlist

    @BindView(R.id.fragment_watchlist_header)
    lateinit var mListHeader: CoinsHeaderView
    @BindView(R.id.fragment_watchlist_empty_container)
    lateinit var mEmptyContainer: View
    @BindView(R.id.fragment_watchlist_recycler)
    lateinit var mRecycler: RecyclerView
    private var mAdapter: WatchlistAdapter? = null

    @BindView(R.id.fragment_watchlist_refresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.fragment_watchlist_error_container)
    lateinit var mErrorContainer: View
    @BindView(R.id.connection_error_retry)
    lateinit var mRetry: View
    @BindView(R.id.fragment_watchlist_error_icon)
    lateinit var mErrorIcon: View

    @BindView(R.id.fragment_watchlist_container)
    lateinit var mContainer: View

    @BindView(R.id.fragment_watchlist_favorite_name)
    lateinit var mFavoriteName: TextView
    @BindView(R.id.fragment_watchlist_favorite_price)
    lateinit var mFavoritePrice: TextView

    //region Chart card

    @BindView(R.id.fragment_watchlist_chart)
    lateinit var mChart: LineChart

    @BindView(R.id.watchlist_chart_picked_container)
    lateinit var mPickedContainer: View
    @BindView(R.id.watchlist_chart_picked)
    lateinit var mPickedPrice: TextView
    @BindView(R.id.fragment_watchlist_chart_progress)
    lateinit var mProgress: View

    //endregion

    private val mChartListener = object : ChartListener() {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            if (mPickedContainer.visibility != View.VISIBLE) {
                mPickedContainer.alpha = 0f
                mPickedContainer.animate()
                        ?.setDuration(300L)
                        ?.alpha(1f)
                        ?.withStartAction { mPickedContainer.visible() }
                        ?.start()
            }

            val date = Date(e?.x?.toLong() ?: 0L)
            mPickedPrice.text = "${date.toMediumFormat()} ${date.toHourFormat()}\n\$${(e?.y ?: 0f).format()}"
        }

        override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
            Handler().postDelayed({
                mPickedContainer.animate()
                        ?.setDuration(300L)
                        ?.alpha(0f)
                        ?.withEndAction { mPickedContainer.invisible() }
                        ?.start()
            }, 200)
        }
        override fun onChartSingleTapped(me: MotionEvent?) {
            activity?.also {
                PickFavoriteActivity.start(it)
            }
        }
    }

    @OnClick(
            R.id.watchlist_menu,
            R.id.connection_error_retry,
            R.id.partial_watchlist_empty_add
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.watchlist_menu -> presenter.onMenuClick()

            R.id.connection_error_retry -> presenter.getCoins()

            R.id.partial_watchlist_empty_add -> presenter.onAddCoinClick()
        }
    }

    //region Lifecycle

    override fun initView(rootView: View) {
        context?.also {
            mContainer.setPadding(0, it.statusBarHeight, 0, 0)
        }

        mAdapter = WatchlistAdapter(arrayListOf(), this)

        mRetry.setOnClickListener {
            presenter.getCoins()
        }

        mSwipeRefreshLayout.setOnRefreshListener {
            presenter.getCoins()
        }

        mRecycler.setHasFixedSize(true)
        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.layoutManager = lm
        mRecycler.adapter = mAdapter

        mChart.init(mChartListener)

        mListHeader.setListener { presenter.onSortClick(it) }
    }

    //endregion

    //region ViewHolder

    override fun onClick(position: Int) {
        presenter.onCoinClick(position)
    }

    //endregion

    //region Chart

    private fun showChartData(data: ChartData) = mChart.loadChartData(
            data,
            R.color.blue_green,
            R.drawable.bg_green_chart
    )

    //endregion

    //region Contract

    override fun showFavoriteCoin(coin: CoinEntity) {
        mFavoriteName.text = coin.symbol
        mFavoritePrice.text = "$${coin.getPrice()?.format()}"

        coin.getPriceChange()?.let {
            context?.also { context ->
                val color: Int = if (it > 0f) {
                    context.getColorRes(R.color.green)
                } else {
                    context.getColorRes(R.color.red)
                }

                mFavoritePrice.setTextColor(color)
            }
        }
    }

    override fun showFavoriteChart(chartData: ChartData) {
        showChartData(chartData)
    }

    override fun openCoinInfo(id: Int) {
        activity?.let { CoinInfoActivity.start(it, id) }
    }

    override fun openAddToWatchlist() {
        activity?.let { AddToWatchlistActivity.start(it) }
    }

    override fun updateCoin(position: Int, coinEntity: CoinEntity) {
        mRecycler.visible()
        mEmptyContainer.hide()
        mAdapter?.updateItem(coinEntity)
    }

    override fun deleteCoin(position: Int) {
        mRecycler.visible()
        mAdapter?.deleteItemAt(position)
    }

    override fun showCoins(coins: List<CoinEntity>) {
        mSwipeRefreshLayout.isRefreshing = false
        mListHeader.visible()
        mRecycler.visible()
        mErrorContainer.hide()
        mAdapter?.setItems(coins)
        mRecycler.scrollToPosition(0)
    }

    override fun showEmpty() {
        mErrorContainer.hide()
        mListHeader.hide()
        mEmptyContainer.visible()
    }

    override fun hideEmpty() {
        mEmptyContainer.hide()
    }

    override fun hideCoinsLoading() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showError(hideList: Boolean) {
        mSwipeRefreshLayout.isRefreshing = false
        mEmptyContainer.hide()

        if (hideList) {
            mErrorContainer.visible()
            mListHeader.hide()
            mRecycler.hide()
        } else {
            showShortToast(context, getString(R.string.message_connection_error))
            mErrorContainer.hide()
            mListHeader.visible()
            mRecycler.visible()
        }
    }

    override fun showCoinsLoading() {
        mSwipeRefreshLayout.isRefreshing = true
        mEmptyContainer.hide()
        mErrorContainer.hide()
    }

    override fun showList() {
        mRecycler.visible()
        mListHeader.visible()
    }

    override fun hideList() {
        mRecycler.hide()
        mListHeader.hide()
    }

    override fun showFavoriteLoading() {
        mErrorIcon.hide()
        mProgress.visible()
        mChart.hide()
    }

    override fun hideFavoriteLoading() {
        mProgress.hide()
        mChart.visible()
    }

    override fun showFavoriteError() {
        mChart.hide()
        mProgress.hide()
        mErrorIcon.visible()
    }

    override fun hideFavoriteError() {
        mErrorIcon.hide()
    }

    override fun showSortType(sortType: CoinsCache.CoinSortEnum) {
        mListHeader.currentSort = sortType
    }

    //endregion

    companion object {
        fun newInstance(): WatchListFragment = WatchListFragment()
    }
}