package com.sgztech.babytracker.data.model

import com.sgztech.babytracker.model.BabySex
import com.sgztech.babytracker.util.LocalDateIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class BabyDtoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("birthday")
    @Serializable(LocalDateIso8601Serializer::class)
    val birthday: LocalDate,
    @SerialName("photoUri")
    val photoUri: String,
    @SerialName("sex")
    val sex: BabySex,
    @SerialName("userId")
    val userId: Int,
)