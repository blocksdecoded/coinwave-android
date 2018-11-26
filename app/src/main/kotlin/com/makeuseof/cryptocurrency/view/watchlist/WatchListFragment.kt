package com.makeuseof.cryptocurrency.view.watchlist

import android.app.Dialog
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.util.format
import com.makeuseof.cryptocurrency.view.currency.CurrencyActivity
import com.makeuseof.cryptocurrency.view.pickfavorite.PickFavoriteActivity
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistAdapter
import com.makeuseof.cryptocurrency.view.watchlist.recycler.WatchlistViewHolder
import com.makeuseof.cryptocurrency.view.widgets.ActionConfirmDialog
import com.makeuseof.cryptocurrency.view.widgets.chart.ChartListener
import com.makeuseof.utils.*
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.Dispatchers
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

    @BindView(R.id.fragment_watchlist_container)
    @JvmField var mContainer: View? = null

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
    @JvmField var mChart: LineChart? = null

    @BindView(R.id.watchlist_chart_picked_container)
    @JvmField var mPickedContainer: View? = null
    @BindView(R.id.watchlist_chart_picked)
    @JvmField var mPickedPrice: TextView? = null
    @BindView(R.id.fragment_watchlist_chart_progress)
    @JvmField var mProgress: View? = null

    //endregion

    override fun onPause() {
        super.onPause()
        mActiveDialog?.dismiss()
    }

    override fun initView(rootView: View) {
        context?.also {
            mContainer?.setPadding(0, DimenUtils.getStatusBarHeight(it), 0, 0)
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

        initChart()
    }

    private fun initChart(){
        mChart?.setTouchEnabled(true)
        mChart?.isDragEnabled = true
        mChart?.setScaleEnabled(true)
        mChart?.setDrawGridBackground(false)
        mChart?.setPinchZoom(true)
        mChart?.description?.isEnabled = false
        mChart?.setDrawBorders(false)
        mChart?.axisLeft?.isEnabled = false
        mChart?.axisRight?.isEnabled = false
        mChart?.xAxis?.isEnabled = false
        mChart?.setBorderWidth(0f)
        mChart?.setViewPortOffsets(0f,50f,0f,50f)
        mChart?.setOnChartValueSelectedListener(mChartListener)
        mChart?.onChartGestureListener = mChartListener
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

    private fun showChartData(data: ChartData) = launchSilent(Dispatchers.Main) {
        mChart?.resetZoom()
        mChart?.zoomOut()
        val entries = arrayListOf<Entry>()

        data.usdChart.forEach {
            try {
                entries.add(
                        Entry(it[0].toFloat(), it[1].toFloat())
                )
            } catch (e: Exception) {
                Lg.d(e.message)
            }
        }

        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawCircleHole(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircles(false)
        dataSet.cubicIntensity = 0.3f
        dataSet.setDrawFilled(true)
        dataSet.lineWidth = 1.2f
        dataSet.setDrawValues(false)

        context?.let {
            dataSet.color = ResourceUtil.getColor(it, R.color.blue_green)
            dataSet.fillDrawable = ContextCompat.getDrawable(it, R.drawable.green_chart_bg)
        }

        mChart?.data = LineData(dataSet)
        mChart?.animateX(1000)
    }

    //endregion

    //region Contract

    override fun showFavoriteCurrency(currency: CurrencyEntity) {
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