package com.sgztech.babytracker.service

import com.sgztech.babytracker.data.model.*
import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @DecodeErrorBody
    @POST("user")
    suspend fun save(
        @Body user: CreateUserDtoRequest,
    ): ApiResult<UserDtoResponse, ErrorResponse>

    @DecodeErrorBody
    @POST("user/auth")
    suspend fun auth(
        @Body credentials: CredentialsDTORequest,
    ): ApiResult<UserTokenDTOResponse, ErrorResponse>

    @DecodeErrorBody
    @POST("user/password/forgot")
    suspend fun forgotPassword(
        @Body forgotPassword: ForgotPasswordDtoRequest,
    ): ApiResult<ForgotPasswordDtoRequest, ErrorResponse>

    @DecodeErrorBody
    @POST("user/password/code")
    suspend fun verifyCode(
        @Body forgotPasswordCode: ForgotPasswordCodeDtoRequest,
    ): ApiResult<ForgotPasswordDtoRequest, ErrorResponse>

    @DecodeErrorBody
    @POST("user/password/reset")
    suspend fun passwordReset(
        @Body resetPassword: ResetPasswordDtoRequest,
    ): ApiResult<ForgotPasswordDtoRequest, ErrorResponse>
}
