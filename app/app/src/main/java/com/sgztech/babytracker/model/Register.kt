package com.sgztech.babytracker.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Register(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @DrawableRes
    val icon: Int,
    val name: String,
    val description: String,
    val localDateTime: LocalDateTime,
    val duration: Long = 0,
    val note: String = "",
    val userId: Int = 0,
)


