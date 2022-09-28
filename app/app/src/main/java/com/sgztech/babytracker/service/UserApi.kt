package com.sgztech.babytracker.service

import com.sgztech.babytracker.data.model.CreateUserDtoRequest
import com.sgztech.babytracker.data.model.ErrorResponse
import com.sgztech.babytracker.data.model.UserDtoResponse
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
}
