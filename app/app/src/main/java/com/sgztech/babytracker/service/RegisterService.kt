package com.sgztech.babytracker.service

import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.model.RegisterDtoRequest
import com.sgztech.babytracker.data.model.RegisterDtoResponse
import com.sgztech.babytracker.model.buildApiToken

class RegisterService(
    private val serviceExecutor: ServiceExecutor,
    private val registerApi: RegisterApi,
    private val preferenceService: PreferenceService,
) {

    suspend fun save(registerDto: RegisterDtoRequest): Result<RegisterDtoResponse, Error> =
        serviceExecutor.execute {
            val token = preferenceService.getUser().buildApiToken()
            registerApi.save(registerDto, token)
        }

    suspend fun find(userId: Int): Result<List<RegisterDtoResponse>, Error> =
        serviceExecutor.execute {
            val token = preferenceService.getUser().buildApiToken()
            registerApi.find(userId, token)
        }
}
