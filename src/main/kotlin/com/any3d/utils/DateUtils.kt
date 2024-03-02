package com.any3d.utils

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtils {
    companion object {
        fun formatDate(date: Date): String {
            return ZonedDateTime.now()
                .withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
        }
    }
}