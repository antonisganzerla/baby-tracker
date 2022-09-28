package com.sgztech.babytracker.service

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.model.CreateUserDtoRequest
import com.sgztech.babytracker.data.model.UserDtoResponse

class UserService(
    private val serviceExecutor: ServiceExecutor,
    private val userApi: UserApi,
) {

    suspend fun save(name: String, email: String, password: String): Result<UserDtoResponse, Error> =
        serviceExecutor.execute {
            userApi.save(
                CreateUserDtoRequest(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = password,
                )
            )
        }
}
