package com.sgztech.babytracker.ui

import com.sgztech.babytracker.util.brazilianLocale
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeFormatter {

    fun format(date: LocalDate, locale: Locale = brazilianLocale()): String {
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", locale)
        return date.format(formatter)
    }

    fun formatHours(dateTime: LocalDateTime, locale: Locale = brazilianLocale()): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", locale)
        return dateTime.format(formatter)
    }

    fun formatHours(hour: Int, minute: Int, locale: Locale = brazilianLocale()): String =
        String.format(locale, "%02d:%02d", hour, minute)
}