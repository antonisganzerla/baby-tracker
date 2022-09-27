package com.sgztech.babytracker.model

import kotlinx.serialization.Serializable

@Serializable
data class RememberMe(
    val value: Boolean,
    val email: String,
)