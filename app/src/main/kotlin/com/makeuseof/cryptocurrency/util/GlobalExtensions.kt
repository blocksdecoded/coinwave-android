package com.makeuseof.cryptocurrency.util

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.Lg
import com.makeuseof.utils.ResourceUtil
import com.makeuseof.utils.isValidIndex

// Created by askar on 7/23/18.
fun List<CurrencyEntity>.findCurrency(currencyEntity: CurrencyEntity, body: ((index: Int) -> Unit)? = null): Int{
    val index = this.indexOfFirst { it.id == currencyEntity.id }
    if(this.isValidIndex(index)){
        body?.invoke(index)
    }
    return index
}

fun ArrayList<CurrencyEntity>.addSortedByRank(currencyEntity: CurrencyEntity){
    this.add(currencyEntity)
    this.sortBy { it.rank }
}

fun TextView.setChangedPercent(percent: Float){
    text = "${if (percent > 0) "+" else ""}$percent%"
    this.setTextColor(
            if(percent >= 0f){
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