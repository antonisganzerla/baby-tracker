package com.sgztech.babytracker.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.R
import com.sgztech.babytracker.data.RegisterUserRepository
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val repository: RegisterUserRepository,
) : BaseViewModel() {

    private val _formState: MutableLiveData<RegisterFormState> = MutableLiveData()
    val formState: LiveData<RegisterFormState> = _formState

    private val _registerAction: MutableLiveData<RequestAction> = MutableLiveData()
    val registerAction: LiveData<RequestAction> = _registerAction

    fun validate(name: String, email: String, password: String, repeatPassword: String) {
        when {
            name.isEmpty() -> _formState.postValue(RegisterFormState.InvalidName(R.string.msg_enter_name))
            email.isEmpty() -> _formState.postValue(RegisterFormState.InvalidEmail(R.string.msg_enter_email))
            emailIsValid(email) -> _formState.postValue(RegisterFormState.InvalidEmail(R.string.msg_enter_valid_email))
            password.isEmpty() -> _formState.postValue(RegisterFormState.InvalidPassword(R.string.msg_enter_password))
            password.length < 8 -> _formState.postValue(RegisterFormState.InvalidPassword(R.string.msg_enter_valid_password))
            password != repeatPassword -> _formState.postValue(RegisterFormState.InvalidRepeatPassword(R.string.msg_password_not_match))
            else -> _formState.postValue(RegisterFormState.Valid)
        }
    }

    private fun emailIsValid(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches().not()

    fun register(name: String, email: String, password: String) {
        _registerAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val response = repository.register(name, email, password, false)
            _registerAction.handleResponse(response)
        }
    }
}

sealed class RegisterFormState {
    class InvalidName(val errorRes: Int) : RegisterFormState()
    class InvalidEmail(val errorRes: Int) : RegisterFormState()
    class InvalidPassword(val errorRes: Int) : RegisterFormState()
    class InvalidRepeatPassword(val errorRes: Int) : RegisterFormState()
    object Valid : RegisterFormState()
}