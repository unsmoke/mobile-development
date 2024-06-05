package com.cpp.unsmoke.utils.helper.ui

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateFormatter {
    companion object DateFormatter {
        fun formatDate(currentDateString: String, targetTimeZone: String): String {
            val instant = Instant.parse(currentDateString)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
                .withZone(ZoneId.of(targetTimeZone))
            return formatter.format(instant)
        }

        fun formatToISO8601(date: Long): String {
            val instant = Instant.ofEpochMilli(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .withZone(ZoneId.of("UTC"))
            return formatter.format(instant)
        }
    }
}