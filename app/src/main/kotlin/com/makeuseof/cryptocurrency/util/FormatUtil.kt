package com.makeuseof.cryptocurrency.util

import java.text.DecimalFormat

// Created by askar on 7/20/18.
object FormatUtil {
    private val decimalFormat = DecimalFormat("###,###,###,###.##")

    fun withSuffix(count: Float): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kmbtpe"[exp - 1])
    }

    fun formatFloat(float: Float): Float{
        return java.lang.Float.valueOf(decimalFormat.format(float))
    }

    fun formatDouble(double: Double): Double{
        return java.lang.Double.valueOf(decimalFormat.format(double))
    }

    fun formatDoubleString(double: Double): String{
        return decimalFormat.format(double)
    }

    fun formatFloatString(float: Float): String{
        return decimalFormat.format(float)
    }
}

fun Float.format(): String{
    return FormatUtil.formatFloatString(this)
}

fun Double.format(): String{
    return FormatUtil.formatDoubleString(this)
}