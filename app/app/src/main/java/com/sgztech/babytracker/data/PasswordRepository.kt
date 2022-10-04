package com.sgztech.babytracker.data

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.model.ForgotPasswordCodeDtoRequest
import com.sgztech.babytracker.data.model.ForgotPasswordDtoRequest
import com.sgztech.babytracker.data.model.ResetPasswordDtoRequest
import com.sgztech.babytracker.service.UserService

class PasswordRepository(
    private val userService: UserService,
) {

    suspend fun forgotPassword(email: String): Result<Unit, Error> =
        userService.forgotPassword(
            ForgotPasswordDtoRequest(
                email = email,
            )
        )

    suspend fun verifyCode(email: String, code: String): Result<Unit, Error> =
        userService.verifyCode(
            ForgotPasswordCodeDtoRequest(
                email = email,
                code = code,
            )
        )

    suspend fun passwordReset(
        email: String,
        code: String,
        password: String,
        confirmPassword: String,
    ): Result<Unit, Error> =
        userService.passwordReset(
            ResetPasswordDtoRequest(
                email = email,
                code = code,
                password = password,
                confirmPassword = confirmPassword,
            )
        )
}