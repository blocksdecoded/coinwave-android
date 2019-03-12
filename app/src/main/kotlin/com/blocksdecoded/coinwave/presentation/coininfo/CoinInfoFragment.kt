package com.blocksdecoded.coinwave.presentation.coininfo

import android.graphics.Color
import android.net.Uri
import android.os.Handler
import androidx.browser.customtabs.CustomTabsIntent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
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
import com.blocksdecoded.coinwave.util.*
import com.blocksdecoded.coinwave.presentation.widgets.LockableScrollView
import com.blocksdecoded.coinwave.presentation.widgets.OptionSelectorView
import com.blocksdecoded.coinwave.presentation.widgets.chart.ChartListener
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.extensions.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

open class CoinInfoFragment : BaseMvpFragment<ICoinInfoContract.Presenter>(),
    ICoinInfoContract.View {
    override val presenter: ICoinInfoContract.Presenter by inject { parametersOf(this@CoinInfoFragment) }
    override val layoutId: Int = R.layout.fragment_coin_info

    @BindView(R.id.fragment_coin_chart)
    lateinit var mChart: LineChart

    @BindView(R.id.coin_scroll_container)
    lateinit var mScrollContainer: LockableScrollView
    @BindView(R.id.fragment_coin_add_to_watchlist)
    lateinit var mWatchlist: ImageView
    @BindView(R.id.fragment_coin_info_icon)
    lateinit var mIcon: ImageView
    @BindView(R.id.fragment_coin_info_name)
    lateinit var mName: TextView
    @BindView(R.id.fragment_coin_info_symbol)
    lateinit var mSymbol: TextView
    @BindView(R.id.coin_price)
    lateinit var mPrice: TextView
    @BindView(R.id.coin_market_cap)
    lateinit var mMarketCap: TextView
    @BindView(R.id.coin_volume_24h)
    lateinit var mVolume24h: TextView
    @BindView(R.id.coin_available_supply)
    lateinit var mAvailableSupply: TextView
    @BindView(R.id.coin_total_supply)
    lateinit var mTotalSupply: TextView
    @BindView(R.id.coin_change_1h)
    lateinit var mChange1h: TextView
    @BindView(R.id.coin_change_1d)
    lateinit var mChange1d: TextView
    @BindView(R.id.coin_change_1w)
    lateinit var mChange1w: TextView

    @BindView(R.id.fragment_coin_chart_error)
    lateinit var mChartErrorImage: View
    @BindView(R.id.coin_chart_picked_container)
    lateinit var mPickedContainer: View
    @BindView(R.id.coin_chart_picked)
    lateinit var mPickedPrice: TextView
    @BindView(R.id.fragment_coin_progress)
    lateinit var mProgress: View
    @BindView(R.id.coin_chart_period)
    lateinit var mChartPeriods: OptionSelectorView

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
            mScrollEnabled = true
        }

        override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
            mScrollEnabled = false
        }
    }

    @OnClick(R.id.coin_graph_icon, R.id.back, R.id.fragment_coin_add_to_watchlist)
    fun onClick(view: View) {
        when (view.id) {
            R.id.coin_graph_icon -> presenter.onGoToWebsiteClick()
            R.id.back -> finishView()
            R.id.fragment_coin_add_to_watchlist -> {
                presenter.onWatchingClick()
                mWatchlist.playAnimation(R.anim.press_scale_anim)
            }
        }
    }
    var mScrollEnabled = true
        set(value) {
            field = value
            mScrollContainer.mScrollable = value
        }

    override fun initView(rootView: View) {
        mChartPeriods.addClickListener { presenter.onPeriodChanged(it) }

        mChart.init(mChartListener)

        activity?.intent?.also { presenter.fetchCurrencyData(CoinInfoActivity.getIdFromIntent(it)) }
    }

    private fun showData(data: ChartData) = mChart.loadChartData(
            data,
            R.color.black,
            R.drawable.bg_dark_chart
    )

    private fun showCurrencyInfo(coinEntity: CoinEntity) {
        mIcon.loadIcon(coinEntity)
        mName.text = coinEntity.name
        mSymbol.text = coinEntity.symbol
        mPrice.text = "$${coinEntity.getPrice()?.format()}"
        mMarketCap.text = "$${coinEntity.getMarketCap()?.format()}"
        mVolume24h.text = "$${coinEntity.getDailyVolume()?.format()}"
        mAvailableSupply.text = coinEntity.circulatingSupply.format()
        mTotalSupply.text = coinEntity.totalSupply.format()

        mChange1d.hide()
        mChange1w.hide()
        mChange1h.setChangedPercent(coinEntity.priceChange)

        setWatchedIcon(coinEntity.isSaved)
    }

    private fun setWatchedIcon(watched: Boolean) {
        if (watched) {
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

    override fun showCurrencyData(coinEntity: CoinEntity) {
        showCurrencyInfo(coinEntity)
    }

    override fun showLoading() {
        mProgress.visible()
        mChart.invisible()
    }

    override fun hideLoading() {
        mProgress.hide()
    }

    override fun showChartError() {
        mChartErrorImage.visible()
        mChart.hide()
        showShortToast(context, getString(R.string.message_connection_error))
    }

    override fun hideChartError() {
        mChartErrorImage.hide()
    }

    //endregion

    companion object {
        fun newInstance(): CoinInfoFragment = CoinInfoFragment()
    }
}