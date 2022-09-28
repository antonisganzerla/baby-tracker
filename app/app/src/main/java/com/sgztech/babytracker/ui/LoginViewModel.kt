package com.sgztech.babytracker.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.model.RememberMe
import com.sgztech.babytracker.model.User
import kotlinx.coroutines.launch

class LoginViewModel(
    private val preferenceService: PreferenceService,
    private val babyRepository: BabyRepository,
) : ViewModel() {

    private val _formState: MutableLiveData<LoginFormState> = MutableLiveData()
    val formState: LiveData<LoginFormState> = _formState

    private val _navigateState: MutableLiveData<NavigateState> = MutableLiveData()
    val navigateState: LiveData<NavigateState> = _navigateState

    fun saveUser(user: User, isSaveMe: Boolean) {
        preferenceService.setUser(user)
        preferenceService.setUserLogged(true)
        preferenceService.setRememberMe(
            RememberMe(
                value = isSaveMe,
                email = if (isSaveMe) user.email else "",
            )
        )
    }

    fun getRememberMe(): RememberMe? =
        preferenceService.getRememberMe()

    fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            _formState.postValue(LoginFormState.InvalidEmail(R.string.msg_enter_email))
            return
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            _formState.postValue(LoginFormState.InvalidEmail(R.string.msg_enter_valid_email))
            return
        }

        if (password.isEmpty()) {
            _formState.postValue(LoginFormState.InvalidPassword(R.string.msg_enter_password))
            return
        }

        if (password.length < 6) {
            _formState.postValue(LoginFormState.InvalidPassword(R.string.msg_enter_valid_password))
            return
        }

        _formState.postValue(LoginFormState.Valid)
    }

    fun navigateToNextScreen(userId: Int) {
        viewModelScope.launch {
            if (babyRepository.exists(userId))
                _navigateState.postValue(NavigateState.MainScreen)
            else
                _navigateState.postValue(NavigateState.BabyFormScreen)
        }
    }
}

sealed class LoginFormState {
    class InvalidEmail(val errorRes: Int) : LoginFormState()
    class InvalidPassword(val errorRes: Int) : LoginFormState()
    object Valid : LoginFormState()
}

sealed class NavigateState {
    object BabyFormScreen : NavigateState()
    object MainScreen : NavigateState()
}