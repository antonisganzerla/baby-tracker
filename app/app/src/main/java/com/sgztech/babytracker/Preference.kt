package com.sgztech.babytracker

import android.content.Context
import android.content.SharedPreferences
import com.sgztech.babytracker.model.Baby
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferenceService(
    private val sharedPreferences: SharedPreferences,
) {

    fun getBaby(): Baby {
        return Json.decodeFromString(getStringValue("session_baby_key"))
    }

    fun setBaby(baby: Baby) {
        val value = Json.encodeToString(baby)
        setStringValue("session_baby_key", value)
    }

    fun getUserLogged(): Boolean {
        return getBooleanValue("logged_user_key")
    }

    fun setUserLogged(value: Boolean) {
        setBooleanValue("logged_user_key", value)
    }

    fun getUserId(): String {
        return getStringValue("session_user_id_key")
    }

    fun setUserId(value: String) {
        setStringValue("session_user_id_key", value)
    }

    fun getUserName(): String {
        return getStringValue("session_user_name_key")
    }

    fun setUserName(value: String) {
        setStringValue("session_user_name_key", value)
    }

    fun getUserEmail(): String {
        return getStringValue("session_user_email_key")
    }

    fun setUserEmail(value: String) {
        setStringValue("session_user_email_key", value)
    }

    fun getUserToken(context: Context): String {
        return getStringValue("session_user_token_key")
    }

    fun setUserToken(value: String) {
        setStringValue("session_user_token_key", value)
    }

    fun getRememberEmail(): String {
        return getStringValue("remember_email")
    }

    fun setRememberEmail(value: String) {
        setStringValue("remember_email", value)
    }

    fun getRememberPassword(): String {
        return getStringValue("remember_password")
    }

    fun setRememberPassword(value: String) {
        setStringValue("remember_password", value)
    }

    fun getRememberCbLogin(): Boolean {
        return getBooleanValue("remember_cb_login")
    }

    fun setRememberCbLogin(value: Boolean) {
        setBooleanValue("remember_cb_login", value)
    }

    private fun setBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
    }

    private fun setStringValue(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getStringValue(key: String): String {
        val value = sharedPreferences.getString(key, DEFAULT_STRING_VALUE)
        return value ?: DEFAULT_STRING_VALUE
    }
}

private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_BOOLEAN_VALUE = false