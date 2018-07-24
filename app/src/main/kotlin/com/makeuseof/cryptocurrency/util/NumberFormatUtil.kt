package com.makeuseof.cryptocurrency.util

import java.text.DecimalFormat
import kotlin.math.roundToInt

// Created by askar on 7/20/18.
object FormatUtil {
    private val decimalFormat = DecimalFormat("###,###,###,###.#####")

    fun withSuffix(count: Float): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kmbtpe"[exp - 1])
    }

    private fun cleanDouble(double: Double): Double{
        return try {
            decimalFormat.format(double).replace(",", "").toDouble()
        } catch (e: Exception) {
            double
        }
    }

    private fun cleanFloat(float: Float): Float = try {
        decimalFormat.format(float).replace(",", "").toFloat()
    } catch (e: Exception) {
        float
    }

    fun formatFloatString(float: Float): String{
        return if (cleanFloat(float) == float.roundToInt().toFloat()){
            float.roundToInt().toString()
        } else {
            if(float < 10f){
                String.format("%.5f", float)
            } else {
                String.format("%.2f", float)
            }
        }
    }
}

fun Float.format(): String{
    return FormatUtil.formatFloatString(this)
}