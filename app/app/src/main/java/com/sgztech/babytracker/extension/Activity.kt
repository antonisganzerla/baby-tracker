package com.sgztech.babytracker.extension

import android.app.Activity
import android.view.WindowManager
import timber.log.Timber

fun Activity.disableUserInteraction() {
    window.disableUserInteraction()
}

fun Activity.enableUserInteraction() {
    window.enableUserInteraction()
}

fun Activity.logInfo(message: String) {
    Timber.tag(this::class.java.name).i(message)
}

fun Activity.logError(message: String) {
    Timber.tag(this::class.java.name).e(message)
}

fun Activity.logError(throwable: Throwable?) {
    Timber.tag(this::class.java.name).e(throwable)
}