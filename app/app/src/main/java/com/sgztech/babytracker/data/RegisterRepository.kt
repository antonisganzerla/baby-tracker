package com.sgztech.babytracker.data

import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDate
import java.time.LocalDateTime

class RegisterRepository {

    private val datasource: MutableList<Register> = fakeData()

    fun load(date: LocalDate): List<Register> =
        datasource.filter { it.time.toLocalDate().isEqual(date) }

    fun add(register: Register) {
        datasource.add(register)
    }

    private fun fakeData(): MutableList<Register> = mutableListOf(
        Register(
            icon = R.drawable.ic_bathtub_24,
            name = "Banho",
            description = "",
            time = LocalDateTime.now().plusHours(1),
        ),
        Register(
            icon = R.drawable.ic_food_bank_24,
            name = "Amamentação",
            description = "Esquerda 15:10",
            time = LocalDateTime.now().plusHours(2),
        ),
    )
}