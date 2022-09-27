package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.dao.BabyDao
import kotlinx.coroutines.launch

class SplashViewModel(
    private val preferenceService: PreferenceService,
    private val babyDao: BabyDao,
) : ViewModel() {

    private val _navigateState: MutableLiveData<SplashNavigateState> = MutableLiveData()
    val navigateState: LiveData<SplashNavigateState> = _navigateState

    fun navigateToNextScreen() {
        viewModelScope.launch {
            if (preferenceService.getUserLogged())
                if (babyDao.exists(preferenceService.getUser().id))
                    _navigateState.postValue(SplashNavigateState.MainScreen)
                else
                    _navigateState.postValue(SplashNavigateState.BabyFormScreen)
            else
                _navigateState.postValue(SplashNavigateState.LoginScreen)
        }
    }
}

sealed class SplashNavigateState {
    object LoginScreen : SplashNavigateState()
    object BabyFormScreen : SplashNavigateState()
    object MainScreen : SplashNavigateState()
}