package com.sgztech.babytracker.arch

sealed class Result<out T, out E> {
    data class Success<out T>(val value: T) : Result<T, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
}

fun <In : Any, Out : Any, E : Any> Result<In, E>.mapSuccess(
    transform: (In) -> Out,
): Result<Out, E> = when (this) {
    is Result.Success -> Result.Success(transform(value))
    is Result.Failure -> this
}

fun <T : Any, In : Any, Out : Any> Result<T, In>.mapFailure(
    transform: (In) -> Out,
): Result<T, Out> = when (this) {
    is Result.Success -> this
    is Result.Failure -> Result.Failure(transform(error))
}