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
}
