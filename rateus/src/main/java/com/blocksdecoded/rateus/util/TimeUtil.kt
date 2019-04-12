package com.blocksdecoded.rateus.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

internal object TimeUtil {
    private fun getLongDateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    fun getDaysBetween(date: String): Int {
        return try {
            val format = getLongDateFormat()
            format.timeZone = TimeZone.getDefault()
            val lDate = format.parse(date)

            val diff = Date().time - lDate.time

            return TimeUnit.MILLISECONDS.toDays(diff).toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun getFormattedDate(): String {
        val format = getLongDateFormat()
        format.timeZone = TimeZone.getDefault()

        return format.format(Date())
    }
}