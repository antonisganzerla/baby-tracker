package com.sgztech.babytracker.service

import com.sgztech.babytracker.data.model.BabyDtoRequest
import com.sgztech.babytracker.data.model.BabyDtoResponse
import com.sgztech.babytracker.data.model.ErrorResponse
import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import retrofit2.http.*

interface BabyApi {

    @DecodeErrorBody
    @POST("baby")
    suspend fun save(
        @Body baby: BabyDtoRequest,
        @Header("Authorization") token: String,
    ): ApiResult<BabyDtoResponse, ErrorResponse>

    @DecodeErrorBody
    @PUT("baby")
    suspend fun update(
        @Body baby: BabyDtoRequest,
        @Header("Authorization") token: String,
    ): ApiResult<BabyDtoResponse, ErrorResponse>

    @DecodeErrorBody
    @GET("baby")
    suspend fun find(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String,
    ): ApiResult<List<BabyDtoResponse>, ErrorResponse>
}