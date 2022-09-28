package com.sgztech.babytracker.service

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.model.CreateUserDtoRequest
import com.sgztech.babytracker.data.model.CredentialsDTORequest
import com.sgztech.babytracker.data.model.UserDtoResponse
import com.sgztech.babytracker.data.model.UserTokenDTOResponse

class UserService(
    private val serviceExecutor: ServiceExecutor,
    private val userApi: UserApi,
) {

    suspend fun save(createUser: CreateUserDtoRequest): Result<UserDtoResponse, Error> =
        serviceExecutor.execute {
            userApi.save(createUser)
        }

    suspend fun auth(credentials: CredentialsDTORequest): Result<UserTokenDTOResponse, Error> =
        serviceExecutor.execute {
            userApi.auth(credentials)
        }
}
