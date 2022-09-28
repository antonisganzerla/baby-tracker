package com.sgztech.babytracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("errors")
    val errors: List<String>
)
