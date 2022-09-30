package com.sgztech.babytracker.arch

import com.sgztech.babytracker.R
import com.sgztech.babytracker.ui.RequestAction

sealed class Error {
    data class Validation(val errors: List<String>) : Error()
    data class Unknown(val exception: Throwable? = null) : Error()
    data class NetWork(val exception: Throwable? = null) : Error()
    data class Auth(val errorRes: Int = R.string.msg_auth_error): Error()
}

fun Error.Unknown.toGenericFailure(): RequestAction.GenericFailure =
    RequestAction.GenericFailure(R.string.msg_unknown_error, exception)

fun Error.NetWork.toGenericFailure(): RequestAction.GenericFailure =
    RequestAction.GenericFailure(R.string.msg_network_error, exception)

fun Error.Validation.toValidationFailure(): RequestAction.ValidationFailure =
    RequestAction.ValidationFailure(errors)

fun Error.Auth.toAuthFailure(): RequestAction.AuthFailure =
    RequestAction.AuthFailure(errorRes)
