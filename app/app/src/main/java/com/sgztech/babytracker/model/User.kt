package com.sgztech.babytracker.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val token: String,
    val photoUri: String? = null,
)
