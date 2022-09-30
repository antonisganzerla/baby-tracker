package com.sgztech.babytracker.service

import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.data.model.ErrorResponse
import com.sgztech.babytracker.arch.Result
import com.slack.eithernet.ApiResult
import java.net.HttpURLConnection

class ServiceExecutor {
    suspend fun <T : Any> execute(
        block: suspend () -> ApiResult<T, ErrorResponse>,
    ): Result<T, Error> = when (val result = block()) {
        is ApiResult.Success -> Result.Success(result.value)
        is ApiResult.Failure.HttpFailure -> result.handleHttpFailure()
        is ApiResult.Failure.ApiFailure -> buildValidationOrUnknownFailure(result.error?.errors)
        is ApiResult.Failure.NetworkFailure -> Result.Failure(Error.NetWork(result.error))
        is ApiResult.Failure.UnknownFailure -> Result.Failure(Error.Unknown(result.error))
    }


    private fun ApiResult.Failure.HttpFailure<ErrorResponse>.handleHttpFailure() =
        when (this.code) {
            HttpURLConnection.HTTP_FORBIDDEN -> Result.Failure(Error.Auth())
            else -> buildValidationOrUnknownFailure(error?.errors)
        }

    private fun buildValidationOrUnknownFailure(errors: List<String>?) =
        errors?.let {
            Result.Failure(Error.Validation(it))
        } ?: Result.Failure(Error.Unknown())

}