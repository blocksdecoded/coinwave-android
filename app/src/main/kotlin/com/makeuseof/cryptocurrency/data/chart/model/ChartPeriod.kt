package com.makeuseof.cryptocurrency.data.chart.model

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
enum class ChartPeriod(
        val representation: String
) {
    DAY("24h"),
    WEEK("7d"),
    MONTH("30d"),
    YEAR("1y"),
    FIVE_YEARS("5y");
}