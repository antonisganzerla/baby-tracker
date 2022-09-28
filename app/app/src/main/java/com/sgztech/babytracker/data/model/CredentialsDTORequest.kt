package com.sgztech.babytracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDTORequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)
