package com.sgztech.babytracker.ui

import com.sgztech.babytracker.util.brazilianLocale
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeFormatter(
    private val locale: Locale = brazilianLocale(),
) {

    fun format(date: LocalDate, pattern: String = "EEE, d MMM yyyy"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return date.format(formatter)
    }

    fun formatHours(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", locale)
        return dateTime.format(formatter)
    }

    fun formatHours(hour: Int, minute: Int): String =
        String.format(locale, "%02d:%02d", hour, minute)
}