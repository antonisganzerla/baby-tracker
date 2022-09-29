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
    private val babyDao: BabyDao,
    private val babyService: BabyService,
) {

    suspend fun loadByUserId(userId: Int): Baby? {
        return babyDao.loadByUserId(userId)
            ?: return when (val response = babyService.find(userId)) {
                is Result.Failure -> null
                is Result.Success -> response.value.firstOrNull()?.run {
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
            }
    }

    suspend fun exists(userId: Int): Boolean =
        babyDao.exists(userId)

    suspend fun save(baby: Baby): Result<BabyDtoResponse, Error> {
        val response = babyService.save(
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
                babyDao.insertAll(baby.copy(webId = response.value.id))
                response
            }
        }
    }

    suspend fun sync(userId: Int) {
        val baby = babyDao.loadByUserIdWithoutSync(userId)
        baby?.let {
            val response = babyService.save(
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
        val response = babyService.update(
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
                babyDao.update(baby)
                response
            }
        }
    }

    suspend fun delete(baby: Baby) {
        babyDao.delete(baby)
    }
}