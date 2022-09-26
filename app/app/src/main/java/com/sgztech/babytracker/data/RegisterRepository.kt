package com.sgztech.babytracker.data

import com.sgztech.babytracker.dao.RegisterDao
import com.sgztech.babytracker.model.Register
import java.time.LocalDate

class RegisterRepository(
    private val dao: RegisterDao,
) {
    suspend fun load(userId: Int, date: LocalDate): List<Register> =
        dao.loadAllByUserId(userId).filter { it.localDateTime.toLocalDate().isEqual(date) }
            .sortedBy { it.localDateTime }

    suspend fun add(register: Register) {
        dao.insertAll(register)
    }

    suspend fun delete(register: Register) {
        dao.delete(register)
    }
}