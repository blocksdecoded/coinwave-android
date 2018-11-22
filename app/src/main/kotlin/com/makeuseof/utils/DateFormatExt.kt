package com.makeuseof.utils

import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

// Created by askar on 9/5/18.
private fun getLongDateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
private fun getMediumFormat(): DateFormat = SimpleDateFormat.getDateInstance(MEDIUM)
private fun getLongFormat(): DateFormat = SimpleDateFormat.getDateInstance(FULL)
private fun getShortFormat(): DateFormat = SimpleDateFormat.getDateInstance(SHORT)
private fun getHourFormat(): SimpleDateFormat = SimpleDateFormat("HH:mm")

object DateFormatExt {
    val cachedShortFormat = getShortFormat()
    val cachedMediumFormat = getMediumFormat()
    val cachedLongFormat = getLongFormat()
    val cachedHourFormat = getHourFormat()
}

fun Date.toMediumFormat(): String = DateFormatExt.cachedMediumFormat.format(this)

fun Date.toLongFormat(): String = DateFormatExt.cachedLongFormat.format(this)

fun Date.toShortFormat(): String = DateFormatExt.cachedShortFormat.format(this)

fun Date.toHourFormat(): String = DateFormatExt.cachedHourFormat.format(this)

fun Date.toStartOfDay(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toStartOfDay
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    return calendar.time
}

fun Date.toEndOfDay(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toEndOfDay
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }

    return calendar.time
}