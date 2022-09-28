package com.sgztech.babytracker.service

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.data.model.ErrorResponse
import com.sgztech.babytracker.arch.Result
import com.slack.eithernet.ApiResult


class ServiceExecutor {
    suspend fun <T : Any> execute(
        block: suspend () -> ApiResult<T, ErrorResponse>,
    ): Result<T, Error> = when (val result = block()) {
        is ApiResult.Success -> Result.Success(result.value)
        is ApiResult.Failure.HttpFailure -> Result.Failure(Error.Validation(result.error?.errors))
        is ApiResult.Failure.ApiFailure -> Result.Failure(Error.Validation(result.error?.errors))
        is ApiResult.Failure.NetworkFailure -> Result.Failure(Error.Unknown(result.error))
        is ApiResult.Failure.UnknownFailure -> Result.Failure(Error.Unknown(result.error))
    }
}