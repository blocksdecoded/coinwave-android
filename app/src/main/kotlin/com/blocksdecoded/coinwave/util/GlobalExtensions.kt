package com.blocksdecoded.coinwave.util

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.view.widgets.chart.ChartListener
import com.blocksdecoded.utils.Lg
import com.blocksdecoded.utils.ResourceUtil
import com.blocksdecoded.utils.isValidIndex

// Created by askar on 7/23/18.
fun List<CoinEntity>.findCurrency(coinEntity: CoinEntity, body: ((index: Int) -> Unit)? = null): Int {
    val index = this.indexOfFirst { it.id == coinEntity.id }
    if (this.isValidIndex(index)) {
        body?.invoke(index)
    }
    return index
}

fun ArrayList<CoinEntity>.addSortedByRank(coinEntity: CoinEntity) {
    this.add(coinEntity)
    this.sortBy { it.rank }
}

fun TextView.setChangedPercent(percent: Float) {
    text = "${if (percent > 0) "+" else ""}$percent%"
    this.setTextColor(
            if (percent >= 0f) {
                ResourceUtil.getColor(this.context, R.color.green)
            } else {
                ResourceUtil.getColor(this.context, R.color.red)
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

    dataSet.color = ResourceUtil.getColor(context, color)
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