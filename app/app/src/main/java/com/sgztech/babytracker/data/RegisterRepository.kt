package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.dao.RegisterDao
import com.sgztech.babytracker.data.model.RegisterDtoRequest
import com.sgztech.babytracker.data.model.RegisterDtoResponse
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.service.RegisterService
import java.time.LocalDate

class RegisterRepository(
    private val dao: RegisterDao,
    private val service: RegisterService,
) {
    suspend fun load(userId: Int, date: LocalDate): Result<List<Register>, Error> {
        val hasRegisters = dao.exists(userId)
        if (hasRegisters.not()) {
            when (val result = service.find(userId)) {
                is Result.Failure -> return result
                is Result.Success -> {
                    val items = result.value.map {
                        Register(
                            icon = it.icon,
                            name = it.name,
                            description = it.description,
                            localDateTime = it.localDateTime,
                            duration = it.duration,
                            note = it.note,
                            userId = it.userId,
                            type = it.type,
                            subType = it.subType,
                            webId = it.id,
                        )
                    }
                    dao.insertAll(*items.toTypedArray())
                }
            }
        }

        val registers =  dao.loadAllByUserId(userId).filter { it.localDateTime.toLocalDate().isEqual(date) }
            .sortedBy { it.localDateTime }
        return Result.Success(registers)
    }

    suspend fun save(register: Register): Result<RegisterDtoResponse, Error> {
        val result = service.save(
            RegisterDtoRequest(
                id = 0,
                icon = register.icon,
                name = register.name,
                description = register.description,
                localDateTime = register.localDateTime,
                duration = register.duration,
                note = register.note,
                type = register.type,
                userId = register.userId,
                subType = register.subType,
            )
        )

        if (result is Result.Success)
            dao.insertAll(register.copy(webId = result.value.id))
        return result
    }

    suspend fun delete(register: Register): Result<Unit, Error> {
        val result = service.delete(
            register.webId ?: 0
        )
        if (result is Result.Success)
            dao.delete(register)
        return result
    }

    suspend fun loadLocal(userId: Int): List<Register> =
        dao.loadAllByUserId(userId)
}