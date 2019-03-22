package com.blocksdecoded.coinwave.util

import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

enum class RelativeDateType {
    NOW, MINUTES_AGO, HOURS_AGO, TODAY, YESTERDAY, DATE_THIS_YEAR, DATE_PAST_YEAR
}

data class RelativeDateInfo(val type: RelativeDateType, val dateString: String)

object DateHelper {

    val serverDateFormat: SimpleDateFormat

    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun getSecondsAgo(date: Date): Long {
        val differenceInMillis = Date().time - date.time
        return differenceInMillis / 1000
    }

    fun getDurationString(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        var videoDuration = ""
        if (hours > 0) {
            videoDuration += twoDigitString(hours) + ":"
        }
        videoDuration += twoDigitString(minutes) + ":" + twoDigitString(seconds % 60)

        return videoDuration
    }

    private fun twoDigitString(number: Int): String {
        if (number == 0) {
            return "00"
        }
        if (number / 10 == 0) {
            return "0$number"
        }
        return number.toString()
    }

    init {
        serverDateFormat = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.US)
        serverDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    @Synchronized
    fun parseDate(dateString: String?): Date? {
        return try {
            dateString?.let { serverDateFormat.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    fun formatDateForServer(date: Date?): String? = date?.let { serverDateFormat.format(it) }

    private fun formatDate(date: Date, outputFormat: String): String {

        if (Build.VERSION.SDK_INT >= 18) {
            return SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), outputFormat)).format(date)
        }

        var of = outputFormat
        if (Locale.getDefault().language == "ru" || Locale.getDefault().language == "ky") {
            of = of.replace("MMMM d", "d MMMM")
            of = of.replace("MMM d", "d MMM")
        }

        return SimpleDateFormat(of, Locale.getDefault()).format(date)
    }

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

    fun getFormattedDate(date: Date) = if (isThisYear(date)) formatDate(date, "MMM d") else formatDate(date, "MMM d, yyyy")

    fun getFormattedDateWithYear(date: Date) = formatDate(date, "MMMM d, yyyy")

    fun getFormattedDateWithoutYear(date: Date) = formatDate(date, "MMMM d")

    fun getFormattedDateShort(date: Date) = formatDate(date, "dd/MM/yy")

    fun getFormattedWeekShort(date: Date) = formatDate(date, "EEE")

    fun getFormattedWeekLong(date: Date) = formatDate(date, "EEEE")

    fun getDateStamp(date: Date): Int {
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.US).format(date)
        return Integer.valueOf(formattedDate)!!
    }

    fun getMonthStamp(date: Date): Int {
        val formattedDate = SimpleDateFormat("yyyyMM", Locale.US).format(date)
        return Integer.valueOf(formattedDate)!!
    }

    private fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        return isSameDay(date, today)
    }

    private fun isYesterday(date: Date): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(date, yesterday)
    }

    private fun isWithinFiveDays(date: Date) = getSecondsAgo(date) < 60 * 60 * 24 * 5

    private fun isSameDay(date: Date, calendar: Calendar): Boolean {
        val calendar2 = Calendar.getInstance()
        calendar2.time = date

        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameDay(date: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val calendar2 = Calendar.getInstance()
        calendar2.time = date2

        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isThisYear(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar2.time = date

        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

    fun withinFiveMinutes(date1: Date, date2: Date): Boolean {
        val differenceInMillis = Math.abs(date2.time - date1.time)
        return (differenceInMillis / 1000 / 60) < 5
    }

    fun durationHumanReadable(duration: Long): String {
        if (duration == 0L) {
            return "00:00"
        }

        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val mins = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration)

        val durationInHuman = if (hours > 0) String.format(Locale.US, "%2d:%02d:%02d", hours, mins % 60, seconds % 60) else String.format(Locale.US, "%02d:%02d", mins, seconds % 60)

        return durationInHuman
    }
}
