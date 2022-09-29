package com.sgztech.babytracker.service

import com.sgztech.babytracker.data.model.ErrorResponse
import com.sgztech.babytracker.data.model.RegisterDtoRequest
import com.sgztech.babytracker.data.model.RegisterDtoResponse
import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import retrofit2.http.*

interface RegisterApi {

    @DecodeErrorBody
    @POST("register")
    suspend fun save(
        @Body register: RegisterDtoRequest,
        @Header("Authorization") token: String,
    ): ApiResult<RegisterDtoResponse, ErrorResponse>

    @DecodeErrorBody
    @GET("register")
    suspend fun find(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String,
    ): ApiResult<List<RegisterDtoResponse>, ErrorResponse>
}