package com.sgztech.babytracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordCodeDtoRequest(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String,
)
