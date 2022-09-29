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
    val userId: Int,
    val webId: Int? = null,
)

fun Baby.getSexEnum(): BabySex =
    when (sex) {
        "Masculino" -> BabySex.MALE
        "Feminino" -> BabySex.FEMALE
        else -> BabySex.OTHER
    }

fun BabySex.toSex(): String =
    when (this) {
        BabySex.MALE -> "Masculino"
        BabySex.FEMALE -> "Feminino"
        BabySex.OTHER -> "Outros"
    }