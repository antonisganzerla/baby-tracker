package com.sgztech.babytracker.database

import androidx.room.TypeConverter
import java.time.*

class Converters {
    @TypeConverter
    fun timestampToLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(localDateTime: LocalDateTime?): String? {
        return localDateTime?.toString()
    }

    @TypeConverter
    fun timestampToLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun localDateToTimestamp(localDate: LocalDate?): String? {
        return localDate?.toString()
    }
}