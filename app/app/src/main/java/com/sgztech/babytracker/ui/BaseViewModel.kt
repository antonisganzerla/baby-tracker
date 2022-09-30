package com.sgztech.babytracker.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.arch.*

open class BaseViewModel : ViewModel() {

    internal fun <T> MutableLiveData<RequestAction>.handleResponse(
        response: Result<T, Error>,
        handleValidation: ((error: Error.Validation) -> Unit)? = null,
    ) {
        when (response) {
            is Result.Failure -> when (response.error) {
                is Error.Unknown -> postValue(response.error.toGenericFailure())
                is Error.Validation -> handleValidation?.invoke(response.error) ?: postValue(
                    response.error.toValidationFailure())
                is Error.NetWork -> postValue(response.error.toGenericFailure())
                is Error.Auth -> postValue(response.error.toAuthFailure())
            }
            is Result.Success -> postValue(RequestAction.Success(response.value))
        }
    }

    fun Result.Failure<Error>.mapToViewError(): RequestAction =
        when (error) {
            is Error.Unknown -> error.toGenericFailure()
            is Error.Validation -> error.toValidationFailure()
            is Error.NetWork -> error.toGenericFailure()
            is Error.Auth -> error.toAuthFailure()
        }
}