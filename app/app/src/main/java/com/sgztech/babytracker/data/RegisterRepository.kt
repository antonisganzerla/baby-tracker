package com.sgztech.babytracker.data

import com.sgztech.babytracker.model.Register
import java.time.LocalDate

class RegisterRepository {

    private val datasource: MutableList<Register> = fakeData()

    fun load(date: LocalDate): List<Register> =
        datasource.filter { it.startTime.toLocalDate().isEqual(date) }.sortedBy { it.startTime }

    fun add(register: Register) {
        datasource.add(register)
    }

    private fun fakeData(): MutableList<Register> = mutableListOf()
}