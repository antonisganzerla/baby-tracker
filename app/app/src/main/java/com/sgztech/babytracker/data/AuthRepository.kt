package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.mapSuccess
import com.sgztech.babytracker.data.model.CredentialsDTORequest
import com.sgztech.babytracker.model.User
import com.sgztech.babytracker.service.UserService

class AuthRepository(
    private val userService: UserService,
) {

    suspend fun auth(email: String, password: String): Result<User, Error> =
        userService.auth(
            credentials = CredentialsDTORequest(
                email = email,
                password = password,
            ),
        ).mapSuccess {
           User(
                id = it.id,
                name = it.name,
                email = it.email,
                token = it.token,
            )
        }
}