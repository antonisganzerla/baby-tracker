package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.dao.BabyDao
import com.sgztech.babytracker.data.model.BabyDtoRequest
import com.sgztech.babytracker.data.model.BabyDtoResponse
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.getSexEnum
import com.sgztech.babytracker.model.toSex
import com.sgztech.babytracker.service.BabyService

class BabyRepository(
    private val dao: BabyDao,
    private val service: BabyService,
) {

    suspend fun loadByUserId(userId: Int): Baby? {
        return dao.loadByUserId(userId)
            ?: return when (val response = service.find(userId)) {
                is Result.Failure -> null
                is Result.Success -> {
                    val baby = response.value.firstOrNull()?.run {
                        Baby(
                            id = 0,
                            name = name,
                            birthday = birthday,
                            sex = sex.toSex(),
                            photoUri = photoUri,
                            userId = userId,
                            webId = id,
                        )
                    }
                    baby?.let { dao.insertAll(it) }
                    baby
                }
            }
    }

    suspend fun exists(userId: Int): Result<Boolean, Error> {
        if (dao.exists(userId).not()) {
            return when (val response = service.find(userId)) {
                is Result.Failure -> response
                is Result.Success -> Result.Success(response.value.isNotEmpty())
            }
        }
        return Result.Success(true)
    }

    suspend fun save(baby: Baby): Result<BabyDtoResponse, Error> {
        val response = service.save(
            BabyDtoRequest(
                id = baby.webId ?: 0,
                name = baby.name,
                birthday = baby.birthday,
                photoUri = baby.photoUri,
                sex = baby.getSexEnum(),
                userId = baby.userId,
            )
        )
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> {
                dao.insertAll(baby.copy(webId = response.value.id))
                response
            }
        }
    }

    suspend fun sync(userId: Int) {
        val baby = dao.loadByUserIdWithoutSync(userId)
        baby?.let {
            val response = service.save(
                BabyDtoRequest(
                    id = 0,
                    name = baby.name,
                    birthday = baby.birthday,
                    photoUri = baby.photoUri,
                    sex = baby.getSexEnum(),
                    userId = baby.userId,
                )
            )
            when (response) {
                is Result.Failure -> {}
                is Result.Success -> update(baby.copy(webId = response.value.id))
            }
        }
    }

    suspend fun update(baby: Baby): Result<BabyDtoResponse, Error> {
        val response = service.update(
            BabyDtoRequest(
                id = baby.webId ?: 0,
                name = baby.name,
                birthday = baby.birthday,
                photoUri = baby.photoUri,
                sex = baby.getSexEnum(),
                userId = baby.userId,
            )
        )
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> {
                dao.update(baby)
                response
            }
        }
    }

    suspend fun delete(baby: Baby) {
        dao.delete(baby)
    }
}