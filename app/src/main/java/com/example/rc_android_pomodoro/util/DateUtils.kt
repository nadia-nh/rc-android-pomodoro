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

    private const val TIMESTAMP_FORMAT_DATE = "MMM d"
    private const val TIMESTAMP_FORMAT_TIME = "h:mm a"
}