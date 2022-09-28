package com.sgztech.babytracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserTokenDTOResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("token")
    val token: String,
)
