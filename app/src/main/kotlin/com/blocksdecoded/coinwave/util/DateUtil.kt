package com.blocksdecoded.coinwave.util

import android.annotation.SuppressLint
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

enum class RelativeDateType {
    NOW, MINUTES_AGO, HOURS_AGO, TODAY, YESTERDAY, DATE_THIS_YEAR, DATE_PAST_YEAR
}

data class RelativeDateInfo(val type: RelativeDateType, val dateString: String)

object DateHelper {
    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val serverDateFormat: SimpleDateFormat = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.US)

    init {
        serverDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    private fun getSecondsAgo(date: Date): Long {
        val differenceInMillis = Date().time - date.time
        return differenceInMillis / 1000
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date: Date, outputFormat: String): String =
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), outputFormat)).format(date)

    fun getRelativeDate(date: Date?) = if (date != null) getRelativeDateInfo(date).dateString else ""

    private fun getRelativeDateInfo(date: Date): RelativeDateInfo {
        val secondsAgo = getSecondsAgo(date).toInt()
        val hoursAgo = secondsAgo / (60 * 60)
        val minutesAgo = secondsAgo / 60

        return when {
            secondsAgo < 60 -> RelativeDateInfo(RelativeDateType.NOW, "just now")
            secondsAgo < 60 * 60 -> RelativeDateInfo(RelativeDateType.MINUTES_AGO, "${minutesAgo}m. ago")
            hoursAgo < 12 -> RelativeDateInfo(RelativeDateType.HOURS_AGO, "${hoursAgo}h. ago")
            isYesterday(date) -> RelativeDateInfo(RelativeDateType.YESTERDAY, "yesterday")
            isThisYear(date) -> RelativeDateInfo(RelativeDateType.DATE_THIS_YEAR, getFormattedDateWithoutYear(date))
            else -> RelativeDateInfo(RelativeDateType.DATE_PAST_YEAR, getFormattedDateWithYear(date))
        }
    }

    private fun getFormattedDateWithYear(date: Date) = formatDate(date, "MMMM d, yyyy")

    private fun getFormattedDateWithoutYear(date: Date) = formatDate(date, "MMMM d")

    private fun isYesterday(date: Date): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(date, yesterday)
    }

    private fun isSameDay(date: Date, calendar: Calendar): Boolean {
        val calendar2 = Calendar.getInstance()
        calendar2.time = date

        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isThisYear(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar2.time = date

        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }
}
