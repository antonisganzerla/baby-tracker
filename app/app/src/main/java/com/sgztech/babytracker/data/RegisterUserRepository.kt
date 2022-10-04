package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.model.CreateUserDtoRequest
import com.sgztech.babytracker.data.model.UserDtoResponse
import com.sgztech.babytracker.service.UserService

class RegisterUserRepository(
    private val userService: UserService,
) {

    suspend fun register(
        name: String,
        email: String,
        password: String,
        isGoogleAccount: Boolean,
    ): Result<UserDtoResponse, Error> =
        userService.save(
            createUser = CreateUserDtoRequest(
                name = name,
                email = email,
                password = password,
                confirmPassword = password,
                isGoogleAccount = isGoogleAccount,
            ),
        )
}
