package com.sgztech.babytracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Baby(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val birthday: LocalDate,
    val sex: String,
    val photoUri: String,
    val userId: Int = 0,
)
