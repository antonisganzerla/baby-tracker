package com.sgztech.babytracker.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.showSnackbar(@StringRes resourceMessage: Int) {
    Snackbar.make(this, resourceMessage, Snackbar.LENGTH_LONG).show()
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.hideKeyBoard() {
    val inputMethodManager =
        this.context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextInputLayout.enableError(errorRes: Int) {
    isErrorEnabled = true
    error = context.getString(errorRes)
}

fun TextInputLayout.disableError() {
    isErrorEnabled = false
    error = ""
}
