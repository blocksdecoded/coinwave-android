package com.blocksdecoded.coinwave.util

import android.util.Log
import com.blocksdecoded.utils.logD
import java.text.DecimalFormat
import kotlin.math.roundToInt

// Created by askar on 7/20/18.
object FormatUtil {
    private val defaultFormat = DecimalFormat("###,###,###,###,###,###,###")
    private val decimalFormat = DecimalFormat("###,###,###,###,###,###,###.00")
    private val smallDecimalFormat = DecimalFormat("###,###,###,###,###,###,##0.00000")

    fun withSuffix(count: Float): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kmbtpe"[exp - 1])
    }

    private fun cleanDouble(double: Double): Double = try {
        decimalFormat.format(double).replace(",", "").toDouble()
    } catch (e: Exception) {
        double
    }

    private fun cleanFloat(float: Float): Float = try {
        decimalFormat.format(float).replace(",", "").toFloat()
    } catch (e: Exception) {
        float
    }

    fun formatFloatString(float: Float): String =
            if (cleanFloat(float) == float.roundToInt().toFloat()) {
                defaultFormat.format(float)
            } else {
                if (float < 10f) {
                    smallDecimalFormat.format(float)
                } else {
                    decimalFormat.format(float)
                }
            }

    fun formatDoubleString(double: Double): String =
        if (double < 10f) {
            smallDecimalFormat.format(double)
        } else {
            if (cleanDouble(double) == double.roundToInt().toDouble()) {
                defaultFormat.format(double)
            } else {
                decimalFormat.format(double)
            }
        }
}

fun Float.format(): String = FormatUtil.formatFloatString(this)

fun Double.format(): String = FormatUtil.formatDoubleString(this)