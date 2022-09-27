package com.sgztech.babytracker.model

import com.sgztech.babytracker.util.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Baby(
    val id: Long = 0,
    val name: String,
    @Serializable(LocalDateIso8601Serializer::class)
    val birthday: LocalDate,
    val sex: String,
    val photoUri: String,
    val userId: Int = 1,
)
