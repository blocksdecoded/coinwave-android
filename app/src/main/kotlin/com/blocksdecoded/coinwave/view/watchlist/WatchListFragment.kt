package com.blocksdecoded.coinwave.view.watchlist

import android.app.Dialog
import android.os.Handler
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.blocksdecoded.core.mvp.BaseMVPFragment
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.util.format
import com.blocksdecoded.coinwave.util.init
import com.blocksdecoded.coinwave.util.loadChartData
import com.blocksdecoded.coinwave.view.currency.CurrencyActivity
import com.blocksdecoded.coinwave.view.pickfavorite.PickFavoriteActivity
import com.blocksdecoded.coinwave.view.watchlist.recycler.WatchlistAdapter
import com.blocksdecoded.coinwave.view.watchlist.recycler.WatchlistViewHolder
import com.blocksdecoded.coinwave.view.widgets.ActionConfirmDialog
import com.blocksdecoded.coinwave.view.widgets.chart.ChartListener
import com.blocksdecoded.utils.*
import java.util.*

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
    lateinit var mRecycler: RecyclerView
    private var mAdapter: WatchlistAdapter? = null

    @BindView(R.id.fragment_watchlist_refresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.fragment_watchlist_empty)
    lateinit var mEmptyText: View

    @BindView(R.id.fragment_watchlist_error)
    lateinit var mErrorContainer: View
    @BindView(R.id.connection_error_retry)
    lateinit var mRetry: View

    @BindView(R.id.fragment_watchlist_container)
    lateinit var mContainer: View

    @BindView(R.id.fragment_watchlist_favorite_name)
    lateinit var mFavoriteName: TextView
    @BindView(R.id.fragment_watchlist_favorite_price)
    lateinit var mFavoritePrice: TextView

    @OnClick(R.id.watchlist_menu)
    fun onClick(view: View) {
        when (view.id) {
            R.id.watchlist_menu -> mPresenter?.onMenuClick()
        }
    }

    private var mActiveDialog: Dialog? = null

    private val mChartListener = object: ChartListener(){
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            if (mPickedContainer?.visibility != View.VISIBLE){
                mPickedContainer?.alpha = 0f
                mPickedContainer?.animate()
                        ?.setDuration(300L)
                        ?.alpha(1f)
                        ?.withStartAction { mPickedContainer.visible() }
                        ?.start()
            }

            val date = Date(e?.x?.toLong()?:0L)
            mPickedPrice?.text = "${date.toMediumFormat()} ${date.toHourFormat()}\n\$${(e?.y ?: 0f).format()}"
        }

        override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
            Handler().postDelayed({
                mPickedContainer?.animate()
                        ?.setDuration(300L)
                        ?.alpha(0f)
                        ?.withEndAction { mPickedContainer.invisible() }
                        ?.start()
            }, 200)
        }
        override fun onChartSingleTapped(me: MotionEvent?) {
            Log.d("ololo", "Chart single tap")
            activity?.also {
                PickFavoriteActivity.start(it)
            }
        }
    }

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

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        context?.also {
            mContainer.setPadding(0, DimenUtils.getStatusBarHeight(it), 0, 0)
        }

        mAdapter = WatchlistAdapter(arrayListOf(), this)

        mRetry.setOnClickListener {
            mPresenter?.getCurrencyList()
        }

        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter?.getCurrencyList()
        }

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler.layoutManager = lm
        mRecycler.adapter = mAdapter

        mChart.init(mChartListener)
    }

    //region ViewHolder

    override fun onPick(position: Int) {
        mPresenter?.onCurrencyPick(position)
    }

    override fun onClick(position: Int) {
        mPresenter?.onCurrencyClick(position)
    }

    //endregion

    //region Chart

    private fun showChartData(data: ChartData) = mChart.loadChartData(
            data,
            R.color.blue_green,
            R.drawable.green_chart_bg
    )

    //endregion

    //region Contract

    override fun showFavoriteCurrency(currency: CurrencyEntity) {
        mFavoriteName?.text = "${currency.symbol}"
        mFavoritePrice?.text = "$${currency.getPrice()?.format()}"

        currency.getPriceChange()?.let {
//            mPriceChange.text = "${if (it > 0) "+" else ""}$it%"

            context?.also { context ->
                val color: Int = if(it > 0f){
//                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_up_green)
                    ResourceUtil.getColor(context, R.color.green)
                } else {
//                mPriceChangeIcon.setImageResource(R.drawable.ic_arrow_down_red)
                    ResourceUtil.getColor(context, R.color.red)
                }

                mFavoritePrice?.setTextColor(color)
//                mPriceChange.setTextColor(color)
            }
        }
    }

    override fun showFavoriteChart(chartData: ChartData) {
        showChartData(chartData)
    }

    override fun showFavoriteLoading() {
        mProgress.visible()
        mChart.hide()
    }

    override fun hideFavoriteLoading() {
        mProgress.hide()
        mChart.visible()
    }

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