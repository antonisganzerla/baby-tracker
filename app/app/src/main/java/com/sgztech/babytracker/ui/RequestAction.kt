package com.sgztech.babytracker.ui

sealed class RequestAction {
    class UnknownFailure(val errorRes: Int, val exception: Throwable?) : RequestAction()
    class ValidationFailure(val errors: List<String>) : RequestAction()
    class Success<T>(val value: T) : RequestAction()
    object Loading : RequestAction()
}