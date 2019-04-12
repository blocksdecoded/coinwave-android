package com.blocksdecoded.utils.extensions

import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

private fun getMediumFormat(): DateFormat = SimpleDateFormat.getDateInstance(MEDIUM)
private fun getHourFormat(): SimpleDateFormat = SimpleDateFormat("HH:mm")

object DateFormatExt {
    val cachedMediumFormat = getMediumFormat()
    val cachedHourFormat = getHourFormat()
}

fun Date.toMediumFormat(): String = DateFormatExt.cachedMediumFormat.format(this)

fun Date.toHourFormat(): String = DateFormatExt.cachedHourFormat.format(this)