package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.PreferenceService

class SplashViewModel(
    private val preferenceService: PreferenceService,
) : ViewModel() {

    private val _navigateState: MutableLiveData<SplashNavigateState> = MutableLiveData()
    val navigateState: LiveData<SplashNavigateState> = _navigateState

    fun navigateToNextScreen() {
        if (preferenceService.getUserLogged())
            if (runCatching { preferenceService.getBaby() }.isSuccess)
                _navigateState.postValue(SplashNavigateState.MainScreen)
            else
                _navigateState.postValue(SplashNavigateState.BabyFormScreen)
        else
            _navigateState.postValue(SplashNavigateState.LoginScreen)
    }
}

sealed class SplashNavigateState {
    object LoginScreen : SplashNavigateState()
    object BabyFormScreen : SplashNavigateState()
    object MainScreen : SplashNavigateState()
}