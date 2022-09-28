package com.sgztech.babytracker.arch

import com.sgztech.babytracker.R
import com.sgztech.babytracker.ui.RequestAction

sealed class Error {
    data class Validation(val errors: List<String>?) : Error()
    data class Unknown(val exception: Throwable?) : Error()
    data class NetWork(val exception: Throwable?) : Error()
}

fun Error.Unknown.toGenericFailure(): RequestAction.GenericFailure =
    RequestAction.GenericFailure(R.string.msg_unknown_error, exception)

fun Error.NetWork.toGenericFailure(): RequestAction.GenericFailure =
    RequestAction.GenericFailure(R.string.msg_network_error, exception)

fun Error.Validation.toValidationFailure(): RequestAction.ValidationFailure =
    RequestAction.ValidationFailure(errors ?: emptyList())
