package com.sgztech.babytracker.ui

sealed class RequestAction {
    class GenericFailure(val errorRes: Int, val exception: Throwable? = null) : RequestAction()
    class ValidationFailure(val errors: List<String>) : RequestAction()
    class Success<T>(val value: T) : RequestAction()
    object Loading : RequestAction()
}