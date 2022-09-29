package com.sgztech.babytracker.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.toGenericFailure
import com.sgztech.babytracker.arch.toValidationFailure

open class BaseViewModel: ViewModel() {

    internal fun <T>MutableLiveData<RequestAction>.handleResponse(
        response: Result<T, Error>,
        handleValidation: ((error: Error.Validation) -> Unit)? = null,
    ) {
        when (response) {
            is Result.Failure -> when (response.error) {
                is Error.Unknown -> postValue(response.error.toGenericFailure())
                is Error.Validation -> handleValidation?.invoke(response.error) ?: postValue(response.error.toValidationFailure())
                is Error.NetWork -> postValue(response.error.toGenericFailure())
            }
            is Result.Success -> postValue(RequestAction.Success(response.value))
        }
    }
}