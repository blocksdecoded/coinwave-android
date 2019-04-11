package com.blocksdecoded.coinwave.util

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.widget.TextView
import com.blocksdecoded.coinwave.GlideApp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.presentation.widgets.chart.ChartListener
import com.blocksdecoded.utils.extensions.getColorRes
import com.blocksdecoded.utils.extensions.isValidIndex
import com.blocksdecoded.utils.glide.SvgSoftwareLayerSetter
import com.blocksdecoded.utils.logE
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

fun List<CoinEntity>.findCurrency(coinEntity: CoinEntity, body: ((index: Int) -> Unit)? = null): Int =
        this.indexOfFirst { it.id == coinEntity.id }.also {
            if (this.isValidIndex(it)) {
                body?.invoke(it)
            }
        }

fun ArrayList<CoinEntity>.addSortedByRank(coinEntity: CoinEntity) {
    this.add(coinEntity)
    this.sortBy { it.rank }
}

fun TextView.setChangedPercent(percent: Float) {
    text = "${if (percent > 0) "+" else ""}$percent%"
    this.setTextColor(
            if (percent >= 0f) {
                getColorRes(R.color.green)
            } else {
                getColorRes(R.color.red)
            }
    )
}

fun LineChart.loadChartData(
    data: ChartData,
    @ColorRes color: Int,
    @DrawableRes backgroundDrawable: Int
) {
    this.resetZoom()
    this.zoomOut()
    val entries = arrayListOf<Entry>()

    data.chart.forEach {
        try {
            entries.add(Entry(it.time.toFloat(), it.price.toFloat()))
        } catch (e: Exception) {
            logE(e)
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

    dataSet.color = context.getColorRes(color)
    dataSet.fillDrawable = ContextCompat.getDrawable(context, backgroundDrawable)

    this.data = LineData(dataSet)
    this.animateX(1000)
}

fun LineChart.init(listener: ChartListener) {
    this.setTouchEnabled(true)
    this.isDragEnabled = true
    this.setScaleEnabled(true)
    this.setDrawGridBackground(false)
    this.setPinchZoom(true)
    this.description?.isEnabled = false
    this.setDrawBorders(false)
    this.axisLeft?.isEnabled = false
    this.axisRight?.isEnabled = false
    this.xAxis?.isEnabled = false
    this.setBorderWidth(0f)
    this.setViewPortOffsets(0f, 50f, 0f, 50f)
    this.setOnChartValueSelectedListener(listener)
    this.onChartGestureListener = listener
}

fun ImageView.loadCoinIcon(coinEntity: CoinEntity) = try {
    GlideApp.with(this)
        .`as`(PictureDrawable::class.java)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(SvgSoftwareLayerSetter())
        .load(coinEntity.iconUrl)
        .into(this)
} catch (e: Exception) {
    logE(e)
}

fun ImageView.loadImage(url: String) {
    GlideApp.with(this)
        .load(url)
        .transition(withCrossFade())
        .thumbnail(0.02f)
        .into(this)
}