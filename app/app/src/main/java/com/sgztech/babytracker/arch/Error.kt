package com.sgztech.babytracker.arch

sealed class Error {
    data class Validation(val errors: List<String>?) : Error()
    object Unknown : Error()
}
