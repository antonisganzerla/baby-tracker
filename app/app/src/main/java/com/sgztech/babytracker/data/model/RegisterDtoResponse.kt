package com.sgztech.babytracker.data.model

import com.sgztech.babytracker.model.RegisterSubType
import com.sgztech.babytracker.model.RegisterType
import com.sgztech.babytracker.util.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class RegisterDtoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("icon")
    val icon: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("localDateTime")
    @Serializable(LocalDateTimeIso8601Serializer::class)
    val localDateTime: LocalDateTime,
    @SerialName("duration")
    val duration: Long,
    @SerialName("note")
    val note: String,
    @SerialName("userId")
    val userId: Int,
    @SerialName("type")
    val type: RegisterType,
    @SerialName("subType")
    val subType: RegisterSubType? = null,
)
