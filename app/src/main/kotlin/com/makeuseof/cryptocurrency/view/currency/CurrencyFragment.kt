package com.makeuseof.cryptocurrency.view.currency

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.utils.ResourceUtil
import com.makeuseof.utils.inflate
import com.makeuseof.utils.showShortToast

class CurrencyFragment :
        BaseMVPFragment<CurrencyContract.Presenter>(),
        CurrencyContract.View,
        OnChartValueSelectedListener {
    override var mPresenter: CurrencyContract.Presenter? = null

    private var mChart: LineChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_currency_info)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?){
        mChart = rootView?.findViewById(R.id.fragment_currency_chart)

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
        mChart?.setOnChartValueSelectedListener(this)

        showData(listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,33,44))
    }

    private fun showData(data: List<Int>){
        val entries = arrayListOf<Entry>()

        data.forEach {
            entries.add(Entry(it.toFloat(), it.toFloat()))
        }

        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawCircles(false)
        dataSet.setDrawFilled(true)
        dataSet.lineWidth = 1f
        dataSet.setDrawValues(false)

        context?.let {
            dataSet.color = ResourceUtil.getColor(it, R.color.green)
            dataSet.fillDrawable = ContextCompat.getDrawable(it, R.drawable.chart_bg)
        }

        mChart?.data = LineData(dataSet)
        mChart?.animateX(2000)
    }

    //region Chart

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        showShortToast(context, e.toString())
    }

    //endregion
}