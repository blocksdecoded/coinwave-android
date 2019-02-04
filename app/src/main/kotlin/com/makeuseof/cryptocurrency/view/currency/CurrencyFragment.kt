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
import com.makeuseof.cryptocurrency.util.loadChartData
import com.makeuseof.cryptocurrency.util.loadIcon
import com.makeuseof.cryptocurrency.util.setChangedPercent
import com.makeuseof.cryptocurrency.view.widgets.LockableScrollView
import com.makeuseof.cryptocurrency.view.widgets.OptionSelectorView
import com.makeuseof.cryptocurrency.view.widgets.chart.ChartListener
import com.makeuseof.utils.*
import java.util.*

open class CurrencyFragment :
        BaseMVPFragment<CurrencyContract.Presenter>(),
        CurrencyContract.View
{
    companion object {
        fun newInstance(): CurrencyFragment = CurrencyFragment()
    }

    override var mPresenter: CurrencyContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_currency_info

    @BindView(R.id.fragment_currency_chart)
    lateinit var mChart: LineChart

    @BindView(R.id.currency_scroll_container)
    lateinit var mScrollContainer: LockableScrollView
    @BindView(R.id.fragment_currency_add_to_watchlist)
    lateinit var mWatchlist: ImageView
    @BindView(R.id.fragment_currency_info_icon)
    lateinit var mIcon: ImageView
    @BindView(R.id.fragment_currency_info_name)
    lateinit var mName: TextView
    @BindView(R.id.fragment_currency_info_symbol)
    lateinit var mSymbol: TextView
    @BindView(R.id.currency_price)
    lateinit var mPrice: TextView
    @BindView(R.id.currency_market_cap)
    lateinit var mMarketCap: TextView
    @BindView(R.id.currency_volume_24h)
    lateinit var mVolume24h: TextView
    @BindView(R.id.currency_available_supply)
    lateinit var mAvailableSupply: TextView
    @BindView(R.id.currency_total_supply)
    lateinit var mTotalSupply: TextView
    @BindView(R.id.currency_change_1h)
    lateinit var mChange1h: TextView
    @BindView(R.id.currency_change_1d)
    lateinit var mChange1d: TextView
    @BindView(R.id.currency_change_1w)
    lateinit var mChange1w: TextView

    @BindView(R.id.currency_chart_picked_container)
    lateinit var mPickedContainer: View
    @BindView(R.id.currency_chart_picked)
    lateinit var mPickedPrice: TextView
    @BindView(R.id.fragment_currency_progress)
    lateinit var mProgress: View
    @BindView(R.id.currency_chart_period)
    lateinit var mChartPeriods: OptionSelectorView

    private val mChartListener = object : ChartListener() {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            if (mPickedContainer.visibility != View.VISIBLE){
                mPickedContainer.alpha = 0f
                mPickedContainer.animate()
                        ?.setDuration(300L)
                        ?.alpha(1f)
                        ?.withStartAction { mPickedContainer.visible() }
                        ?.start()
            }

            val date = Date(e?.x?.toLong()?:0L)
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
            mScrollEnabled = true
        }

        override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
            mScrollEnabled = false
        }
    }

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
            mScrollContainer.mScrollable = value
        }

    override fun initView(rootView: View){
        mChartPeriods.addClickListener { mPresenter?.onPeriodChanged(it) }

        initChart()
    }

    private fun initChart(){
        mChart.setTouchEnabled(true)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setDrawGridBackground(false)
        mChart.setPinchZoom(true)
        mChart.description?.isEnabled = false
        mChart.setDrawBorders(false)
        mChart.axisLeft?.isEnabled = false
        mChart.axisRight?.isEnabled = false
        mChart.xAxis?.isEnabled = false
        mChart.setBorderWidth(0f)
        mChart.setViewPortOffsets(0f,50f,0f,50f)
        mChart.setOnChartValueSelectedListener(mChartListener)
        mChart.onChartGestureListener = mChartListener
    }

    private fun showData(data: ChartData) = mChart.loadChartData(
            data,
            R.color.black,
            R.drawable.dark_chart_bg
    )

    private fun showCurrencyInfo(currencyEntity: CurrencyEntity){
        mIcon.loadIcon(currencyEntity)
        mName.text = currencyEntity.name
        mSymbol.text = currencyEntity.symbol
        mPrice.text = "$${currencyEntity.getPrice()?.format()}"
        mMarketCap.text = "$${currencyEntity.getMarketCap()?.format()}"
        mVolume24h.text = "$${currencyEntity.getDailyVolume()?.format()}"
        mAvailableSupply.text = currencyEntity.circulatingSupply.format()
        mTotalSupply.text = currencyEntity.totalSupply.format()

        mChange1d.hide()
        mChange1w.hide()
        mChange1h.setChangedPercent(currencyEntity.priceChange)
//        currencyEntity.getUsdQuotes()?.hourChange?.let{
//            mChange1h?.setChangedPercent(it)
//        }
//        currencyEntity.getUsdQuotes()?.dayChange?.let{
//            mChange1d?.setChangedPercent(it)
//        }
//        currencyEntity.getUsdQuotes()?.weekChange?.let{
//            mChange1w?.setChangedPercent(it)
//        }

        setWatchedIcon(currencyEntity.isSaved)
    }

    private fun setWatchedIcon(watched: Boolean){
        if (watched){
            mWatchlist.setImageResource(R.drawable.ic_star_filled)
        } else {
            mWatchlist.setImageResource(R.drawable.ic_star_border)
        }
    }

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