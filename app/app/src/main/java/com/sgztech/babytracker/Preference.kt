package com.sgztech.babytracker

import android.content.SharedPreferences
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.RememberMe
import com.sgztech.babytracker.model.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferenceService(
    private val sharedPreferences: SharedPreferences,
) {

    fun getUser(): User {
        return Json.decodeFromString(getStringValue("session_user_key"))
    }

    fun setUser(user: User) {
        val value = Json.encodeToString(user)
        setStringValue("session_user_key", value)
    }

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

    fun setRememberMe(rememberMe: RememberMe) {
        val value = Json.encodeToString(rememberMe)
        setStringValue("remember_me_key", value)
    }

    fun getRememberMe(): RememberMe? {
        return runCatching { Json.decodeFromString<RememberMe>(getStringValue("remember_me_key")) }.getOrNull()
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