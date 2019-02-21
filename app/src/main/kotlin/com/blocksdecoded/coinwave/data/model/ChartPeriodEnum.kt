package com.blocksdecoded.coinwave.data.model

/**
 * Created by askar on 1/30/19
 * with Android Studio
 */
enum class ChartPeriodEnum(
    val displayName: String
) {
    DAY("24h"),
    WEEK("7d"),
    MONTH("30d"),
    YEAR("1y"),
    FIVE_YEARS("5y");
}