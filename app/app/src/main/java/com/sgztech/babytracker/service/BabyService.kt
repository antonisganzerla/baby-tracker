package com.sgztech.babytracker.service

import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.mapSuccess
import com.sgztech.babytracker.data.model.BabyDtoRequest
import com.sgztech.babytracker.data.model.BabyDtoResponse
import com.sgztech.babytracker.model.buildApiToken

class BabyService(
    private val serviceExecutor: ServiceExecutor,
    private val babyApi: BabyApi,
    private val preferenceService: PreferenceService,
) {

    suspend fun save(babyDtoRequest: BabyDtoRequest): Result<BabyDtoResponse, Error> =
        serviceExecutor.execute {
            val token = preferenceService.getUser().buildApiToken()
            babyApi.save(babyDtoRequest, token)
        }

    suspend fun update(babyDtoRequest: BabyDtoRequest): Result<Int, Error> =
        serviceExecutor.execute {
            val token = preferenceService.getUser().buildApiToken()
            babyApi.update(babyDtoRequest.id, babyDtoRequest, token)
        }

    suspend fun find(userId: Int): Result<List<BabyDtoResponse>, Error> =
        serviceExecutor.execute {
            val token = preferenceService.getUser().buildApiToken()
            babyApi.find(userId, token)
        }
}
