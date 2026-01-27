package com.example.rc_android_pomodoro.util

object DateUtils {
    fun formatTimestampDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat(
            TIMESTAMP_FORMAT_DATE,
            java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun formatTimestampTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat(
            TIMESTAMP_FORMAT_TIME,
            java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun minutesToMillis(minutes: Int): Long {
        return minutes * MILLIS_IN_MINUTE
    }

    fun secondsToMillis(seconds: Int): Long {
        return seconds * MILLIS_IN_SECOND
    }

    fun millisToMinutes(millis: Long): Int {
        return (millis / MILLIS_IN_MINUTE).toInt()
    }

    fun millisToSeconds(millis: Long): Int {
        return (millis / MILLIS_IN_SECOND % 60).toInt()
    }

    private const val TIMESTAMP_FORMAT_DATE = "MMM d"
    private const val TIMESTAMP_FORMAT_TIME = "h:mm a"
    private const val MILLIS_IN_SECOND = 1_000L
    private const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND
}