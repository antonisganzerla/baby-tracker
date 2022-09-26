package com.sgztech.babytracker.ui

import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.PreferenceService

class LoginViewModel(
    private val preferenceService: PreferenceService,
) : ViewModel() {

    fun saveUser(id: Int = 1) {
        preferenceService.setUserId(id.toString())
    }
}