package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.mapSuccess
import com.sgztech.babytracker.data.model.CreateUserDtoRequest
import com.sgztech.babytracker.model.User
import com.sgztech.babytracker.service.UserService

class RegisterUserRepository(
    private val userService: UserService,
) {

    suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Result<User, Error> =
        userService.save(
            createUser = CreateUserDtoRequest(
                name = name,
                email = email,
                password = password,
                confirmPassword = password,
            ),
        ).mapSuccess {
            User(
                id = it.id,
                name = it.name,
                email = it.email,
                token = "",
            )
        }
}
