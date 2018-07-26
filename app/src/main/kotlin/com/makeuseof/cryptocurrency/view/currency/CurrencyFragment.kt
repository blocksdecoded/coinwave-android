package com.makeuseof.cryptocurrency.view.currency

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
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
import com.makeuseof.cryptocurrency.util.loadIcon
import com.makeuseof.cryptocurrency.util.setChangedPercent
import com.makeuseof.cryptocurrency.view.widgets.OptionSelectorView
import com.makeuseof.utils.*

class CurrencyFragment :
        BaseMVPFragment<CurrencyContract.Presenter>(),
        CurrencyContract.View,
        OnChartValueSelectedListener,
        OnChartGestureListener {
    override var mPresenter: CurrencyContract.Presenter? = null

    private var mChart: LineChart? = null

    private var mScrollContainer: ScrollView? = null
    private var mBack: View? = null
    private var mWatchlist: ImageView? = null
    private var mIcon: ImageView? = null
    private var mName: TextView? = null
    private var mSymbol: TextView? = null
    private var mPrice: TextView? = null
    private var mMarketCap: TextView? = null
    private var mVolume24h: TextView? = null
    private var mAvailableSupply: TextView? = null
    private var mTotalSupply: TextView? = null
    private var mChange1h: TextView? = null
    private var mChange1d: TextView? = null
    private var mChange1w: TextView? = null

    private var mProgress: View? = null
    private var mChartPeriods: OptionSelectorView? = null
    private var mScrollEnabled = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_currency_info)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?){
        mChartPeriods = rootView?.findViewById(R.id.currency_chart_period)
        mChartPeriods?.addClickListener { mPresenter?.onPeriodChanged(it) }

        mBack = rootView?.findViewById(R.id.back)
        mWatchlist = rootView?.findViewById(R.id.fragment_currency_add_to_watchlist)
        mIcon = rootView?.findViewById(R.id.fragment_currency_info_icon)
        mName = rootView?.findViewById(R.id.fragment_currency_info_name)
        mSymbol = rootView?.findViewById(R.id.fragment_currency_info_symbol)
        mPrice = rootView?.findViewById(R.id.currency_price)
        mMarketCap = rootView?.findViewById(R.id.currency_market_cap)
        mVolume24h = rootView?.findViewById(R.id.currency_volume_24h)
        mAvailableSupply = rootView?.findViewById(R.id.currency_available_supply)
        mTotalSupply = rootView?.findViewById(R.id.currency_total_supply)
        mChange1h = rootView?.findViewById(R.id.currency_change_1h)
        mChange1d = rootView?.findViewById(R.id.currency_change_1d)
        mChange1w = rootView?.findViewById(R.id.currency_change_1w)
        mProgress = rootView?.findViewById(R.id.fragment_currency_progress)

        mBack?.setOnClickListener { finishView() }
        mWatchlist?.setOnClickListener { mPresenter?.onWatchingClick() }

        mChart = rootView?.findViewById(R.id.fragment_currency_chart)

        initChart()

        mScrollContainer = rootView?.findViewById(R.id.currency_scroll_container)
        mScrollContainer?.setOnTouchListener { v, event ->
            return@setOnTouchListener !mScrollEnabled
        }
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
        mChart?.setOnChartValueSelectedListener(this)
        mChart?.onChartGestureListener = this
    }

    private fun showData(data: ChartData){
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
            dataSet.color = ResourceUtil.getColor(it, R.color.green)
            dataSet.fillDrawable = ContextCompat.getDrawable(it, R.drawable.chart_bg)
        }

        mChart?.data = LineData(dataSet)
        mChart?.animateX(1500)
    }

    private fun showCurrencyInfo(currencyEntity: CurrencyEntity){
        mIcon?.loadIcon(currencyEntity)
        mName?.text = currencyEntity.name
        mSymbol?.text = currencyEntity.symbol
        mPrice?.text = "$${currencyEntity.getPrice()?.format()}"
        mMarketCap?.text = "$${currencyEntity.getMarketCap()?.format()}"
        mVolume24h?.text = "$${currencyEntity.getDailyVolume()?.format()}"
        mAvailableSupply?.text = currencyEntity.circulatingSupply.format()
        mTotalSupply?.text = currencyEntity.totalSupply.format()

        currencyEntity.getUsdQuotes()?.hourChange?.let{
            mChange1h?.setChangedPercent(it)
        }
        currencyEntity.getUsdQuotes()?.dayChange?.let{
            mChange1d?.setChangedPercent(it)
        }
        currencyEntity.getUsdQuotes()?.weekChange?.let{
            mChange1w?.setChangedPercent(it)
        }

        setWatchedIcon(currencyEntity.isSaved)
    }

    private fun setWatchedIcon(watched: Boolean){
        if (watched){
            mWatchlist?.setImageResource(R.drawable.ic_star_filled)
        } else {
            mWatchlist?.setImageResource(R.drawable.ic_star_border)
        }
    }

    //region Chart

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        showShortToast(context, e.toString())
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        Lg.d("End ")
        mScrollEnabled = true
    }

    override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {

    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        Lg.d("Start ")
        mScrollEnabled = false
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
    }

    override fun onChartLongPressed(me: MotionEvent?) {
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
    }

    //endregion

    //region Contract

    override fun setWatched(watched: Boolean) {
        setWatchedIcon(watched)
    }

    override fun showChartData(chartData: ChartData) {
        mProgress.hide()
        mChart.visible()
        showData(chartData)
    }

    override fun showCurrencyData(currencyEntity: CurrencyEntity) {
        showCurrencyInfo(currencyEntity)
    }

    override fun showChartLoading() {
        mProgress.visible()
        mChart.invisible()
    }

    //endregion
}