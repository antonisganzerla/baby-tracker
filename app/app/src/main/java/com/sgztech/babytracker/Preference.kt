package com.sgztech.babytracker

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.annotation.StringRes
import com.sgztech.babytracker.model.Baby
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Context.getBaby(): Baby {
    val key = getKey(this, R.string.session_baby_key)
    return Json.decodeFromString(getStringValue(key))
}

fun Context.setBaby(baby: Baby) {
    val value = Json.encodeToString(baby)
    val key = getKey(this, R.string.session_baby_key)
    setStringValue(key, value)
}

fun Context.getUserLogged(): Boolean {
    val key = getKey(this, R.string.logged_user_key)
    return getBooleanValue(key)
}

fun Context.setUserLogged(value: Boolean) {
    val key = getKey(this, R.string.logged_user_key)
    setBooleanValue(key, value)
}

fun Context.getUserId(): String {
    val key = getKey(this, R.string.session_user_id_key)
    return getStringValue(key)
}

fun Context.setUserId(value: String) {
    val key = getKey(this, R.string.session_user_id_key)
    setStringValue(key, value)
}

fun Context.getUserName(): String {
    val key = getKey(this, R.string.session_user_name_key)
    return getStringValue(key)
}

fun Context.setUserName(value: String) {
    val key = getKey(this, R.string.session_user_name_key)
    setStringValue(key, value)
}

fun Context.getUserEmail(): String {
    val key = getKey(this, R.string.session_user_email_key)
    return getStringValue(key)
}

fun Context.setUserEmail(value: String) {
    val key = getKey(this, R.string.session_user_email_key)
    setStringValue(key, value)
}

fun Context.getUserToken(context: Context): String {
    val key = getKey(this, R.string.session_user_token_key)
    return getStringValue(key)
}

fun Context.setUserToken(value: String) {
    val key = getKey(this, R.string.session_user_token_key)
    setStringValue(key, value)
}

fun Context.getUserSecurityToken(): String {
    val key = getKey(this, R.string.session_user_token_key)
    return getStringValue(key)
}

fun Context.setUserSecurityToken(value: String) {
    val key = getKey(this, R.string.session_user_token_key)
    setStringValue(key, value)
}

fun Context.getRememberEmail(): String {
    val key = getKey(this, R.string.remember_email)
    return getStringValue(key)
}

fun Context.setRememberEmail(value: String) {
    val key = getKey(this, R.string.remember_email)
    setStringValue(key, value)
}

fun Context.getRememberPassword(): String {
    val key = getKey(this, R.string.remember_password)
    return getStringValue(key)
}

fun Context.setRememberPassword(value: String) {
    val key = getKey(this, R.string.remember_password)
    setStringValue(key, value)
}

fun Context.getRememberCbLogin(): Boolean {
    val key = getKey(this, R.string.remember_cb_login)
    return getBooleanValue(key)
}

fun Context.setRememberCbLogin(value: Boolean) {
    val key = getKey(this, R.string.remember_cb_login)
    setBooleanValue(key, value)
}

private fun Context.setBooleanValue(key: String, value: Boolean) {
    val editor = sharedPreferences(this).edit()
    editor.putBoolean(key, value)
    editor.apply()
}

private fun Context.getBooleanValue(key: String): Boolean {
    return sharedPreferences(this).getBoolean(key, DEFAULT_BOOLEAN_VALUE)
}

private fun Context.setStringValue(key: String, value: String) {
    val editor = sharedPreferences(this).edit()
    editor.putString(key, value)
    editor.apply()
}

private fun Context.getStringValue(key: String): String {
    val value = sharedPreferences(this).getString(key, DEFAULT_STRING_VALUE)
    return value ?: DEFAULT_STRING_VALUE
}

private fun getKey(context: Context, @StringRes code: Int): String {
    return context.getString(code)
}

private fun sharedPreferences(context: Context) =
    PreferenceManager.getDefaultSharedPreferences(context)

private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_BOOLEAN_VALUE = false