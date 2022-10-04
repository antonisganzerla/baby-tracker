package com.sgztech.babytracker.service

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.mapSuccess
import com.sgztech.babytracker.data.model.*

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

    suspend fun forgotPassword(forgotPassword: ForgotPasswordDtoRequest): Result<Unit, Error> =
        serviceExecutor.execute {
            userApi.forgotPassword(forgotPassword)
        }.mapSuccess {  }

    suspend fun verifyCode(forgotPasswordCode: ForgotPasswordCodeDtoRequest): Result<Unit, Error> =
        serviceExecutor.execute {
            userApi.verifyCode(forgotPasswordCode)
        }.mapSuccess {  }

    suspend fun passwordReset(resetPassword: ResetPasswordDtoRequest): Result<Unit, Error> =
        serviceExecutor.execute {
            userApi.passwordReset(resetPassword)
        }.mapSuccess {  }
}
