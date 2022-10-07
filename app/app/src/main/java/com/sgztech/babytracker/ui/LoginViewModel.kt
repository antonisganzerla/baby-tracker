package com.sgztech.babytracker.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.toValidationFailure
import com.sgztech.babytracker.data.AuthRepository
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.data.RegisterUserRepository
import com.sgztech.babytracker.model.RememberMe
import com.sgztech.babytracker.model.User
import kotlinx.coroutines.launch

class LoginViewModel(
    private val preferenceService: PreferenceService,
    private val babyRepository: BabyRepository,
    private val authRepository: AuthRepository,
    private val registerUserRepository: RegisterUserRepository,
) : BaseViewModel() {

    private val _formState: MutableLiveData<LoginFormState> = MutableLiveData()
    val formState: LiveData<LoginFormState> = _formState

    private val _navigateState: MutableLiveData<NavigateState> = MutableLiveData()
    val navigateState: LiveData<NavigateState> = _navigateState

    private val _authAction: MutableLiveData<RequestAction> = MutableLiveData()
    val authAction: LiveData<RequestAction> = _authAction

    fun saveUserSession(user: User, isSaveMe: Boolean) {
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
        when {
            email.isEmpty() -> _formState.postValue(LoginFormState.InvalidEmail(R.string.msg_enter_email))
            emailIsValid(email) -> _formState.postValue(LoginFormState.InvalidEmail(R.string.msg_enter_valid_email))
            password.isEmpty() -> _formState.postValue(LoginFormState.InvalidPassword(R.string.msg_enter_password))
            password.length < 8 -> _formState.postValue(LoginFormState.InvalidPassword(R.string.msg_enter_valid_password))
            else -> _formState.postValue(LoginFormState.Valid)
        }
    }

    private fun emailIsValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches().not()

    fun navigateToNextScreen(userId: Int) {
        viewModelScope.launch {
            when (val response = babyRepository.exists(userId)) {
                is Result.Failure -> _navigateState.postValue(NavigateState.Failure(response.mapToViewError()))
                is Result.Success -> if (response.value)
                    _navigateState.postValue(NavigateState.MainScreen)
                else
                    _navigateState.postValue(NavigateState.BabyFormScreen)
            }
        }
    }

    fun auth(email: String, password: String) {
        _authAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val response = authRepository.auth(email, password)
            _authAction.handleResponse(response)
        }
    }

    fun authWithGoogle(name: String, email: String, token: String) {
        _authAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val response = registerUserRepository.register(name, email, token, true)
            if (response is Result.Success)
                auth(email, token)
            else {
                _authAction.handleResponse(response) { error ->
                    if (error.errors.contains(EMAIL_ALREADY_IN_USE_ERROR_PT))
                        auth(email, token)
                    else
                        _authAction.postValue(error.toValidationFailure())
                }
            }
        }
    }
}

private const val EMAIL_ALREADY_IN_USE_ERROR_PT = "Email já está em uso"

sealed class LoginFormState {
    class InvalidEmail(val errorRes: Int) : LoginFormState()
    class InvalidPassword(val errorRes: Int) : LoginFormState()
    object Valid : LoginFormState()
}

sealed class NavigateState {
    object BabyFormScreen : NavigateState()
    object MainScreen : NavigateState()
    class Failure(val error: RequestAction) : NavigateState()
}
