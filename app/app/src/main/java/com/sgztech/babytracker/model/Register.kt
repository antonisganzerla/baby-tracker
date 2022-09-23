package com.sgztech.babytracker.model

import androidx.annotation.DrawableRes
import java.time.LocalDateTime

data class Register(
    @DrawableRes
    val icon: Int,
    val name: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val note: String = "",
)


