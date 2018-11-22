package com.makeuseof.cryptocurrency.view.currency

import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
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
import com.makeuseof.cryptocurrency.view.widgets.LockableScrollView
import com.makeuseof.cryptocurrency.view.widgets.OptionSelectorView
import com.makeuseof.utils.*
import java.util.*

open class CurrencyFragment :
        BaseMVPFragment<CurrencyContract.Presenter>(),
        CurrencyContract.View,
        OnChartValueSelectedListener,
        OnChartGestureListener {
    companion object {
        fun newInstance(): CurrencyFragment = CurrencyFragment()
    }

    override var mPresenter: CurrencyContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_currency_info

    @BindView(R.id.fragment_currency_chart)
    @JvmField var mChart: LineChart? = null

    @BindView(R.id.currency_scroll_container)
    @JvmField var mScrollContainer: LockableScrollView? = null
    @BindView(R.id.fragment_currency_add_to_watchlist)
    @JvmField var mWatchlist: ImageView? = null
    @BindView(R.id.fragment_currency_info_icon)
    @JvmField var mIcon: ImageView? = null
    @BindView(R.id.fragment_currency_info_name)
    @JvmField var mName: TextView? = null
    @BindView(R.id.fragment_currency_info_symbol)
    @JvmField var mSymbol: TextView? = null
    @BindView(R.id.currency_price)
    @JvmField var mPrice: TextView? = null
    @BindView(R.id.currency_market_cap)
    @JvmField var mMarketCap: TextView? = null
    @BindView(R.id.currency_volume_24h)
    @JvmField var mVolume24h: TextView? = null
    @BindView(R.id.currency_available_supply)
    @JvmField var mAvailableSupply: TextView? = null
    @BindView(R.id.currency_total_supply)
    @JvmField var mTotalSupply: TextView? = null
    @BindView(R.id.currency_change_1h)
    @JvmField var mChange1h: TextView? = null
    @BindView(R.id.currency_change_1d)
    @JvmField var mChange1d: TextView? = null
    @BindView(R.id.currency_change_1w)
    @JvmField var mChange1w: TextView? = null

    @BindView(R.id.currency_chart_picked_container)
    @JvmField var mPickedContainer: View? = null
    @BindView(R.id.currency_chart_picked)
    @JvmField var mPickedPrice: TextView? = null

    @BindView(R.id.fragment_currency_progress)
    @JvmField var mProgress: View? = null
    @BindView(R.id.currency_chart_period)
    @JvmField var mChartPeriods: OptionSelectorView? = null

    @OnClick(R.id.currency_graph_icon, R.id.back, R.id.fragment_currency_add_to_watchlist)
    fun onClick(view: View){
        when(view.id) {
            R.id.currency_graph_icon -> mPresenter?.onGoToWebsiteClick()
            R.id.back -> finishView()
            R.id.fragment_currency_add_to_watchlist -> mPresenter?.onWatchingClick()
        }
    }
    var mScrollEnabled = true
        set(value) {
            field = value
            mScrollContainer?.mScrollable = value
        }

    override fun initView(rootView: View){
        mChartPeriods?.addClickListener { mPresenter?.onPeriodChanged(it) }

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
//            dataSet.color = ResourceUtil.getColor(it, R.color.green)
            dataSet.color = ResourceUtil.getColor(it, R.color.black)
            dataSet.fillDrawable = ContextCompat.getDrawable(it, R.drawable.chart_bg)
        }

        mChart?.data = LineData(dataSet)
        mChart?.animateX(1500)
//        mChart?.animateY(1500)
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
        mScrollEnabled = true
    }

    override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {

    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
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

    override fun openSite(url: String) {
        activity?.let {
            CustomTabsIntent
                    .Builder()
                    .setToolbarColor(Color.WHITE)
                    .setStartAnimations(it, R.anim.slide_in_right, R.anim.slide_out_left)
                    .setExitAnimations(it, R.anim.slide_in_left, R.anim.slide_out_right)
                    .build()
                    .launchUrl(it, Uri.parse(url))
        }
    }

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