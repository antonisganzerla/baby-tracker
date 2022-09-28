package com.sgztech.babytracker.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.toValidationFailure
import com.sgztech.babytracker.arch.toGenericFailure
import com.sgztech.babytracker.data.RegisterUserRepository
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val repository: RegisterUserRepository,
) : ViewModel() {

    private val _formState: MutableLiveData<RegisterFormState> = MutableLiveData()
    val formState: LiveData<RegisterFormState> = _formState

    private val _registerAction: MutableLiveData<RequestAction> = MutableLiveData()
    val registerAction: LiveData<RequestAction> = _registerAction

    fun validate(name: String, email: String, password: String, repeatPassword: String) {

        if (name.isEmpty()) {
            _formState.postValue(RegisterFormState.InvalidName(R.string.msg_enter_name))
            return
        }

        if (email.isEmpty()) {
            _formState.postValue(RegisterFormState.InvalidEmail(R.string.msg_enter_email))
            return
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            _formState.postValue(RegisterFormState.InvalidEmail(R.string.msg_enter_valid_email))
            return
        }

        if (password.isEmpty()) {
            _formState.postValue(RegisterFormState.InvalidPassword(R.string.msg_enter_password))
            return
        }

        if (password.length < 8) {
            _formState.postValue(RegisterFormState.InvalidPassword(R.string.msg_enter_valid_password))
            return
        }

        if (password != repeatPassword) {
            _formState.postValue(RegisterFormState.InvalidRepeatPassword(R.string.msg_password_not_match))
            return
        }

        _formState.postValue(RegisterFormState.Valid)
    }

    fun register(name: String, email: String, password: String) {
        _registerAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            when (val response = repository.register(name, email, password)) {
                is Result.Failure -> {
                    when (response.error) {
                        is Error.Unknown -> _registerAction.postValue(response.error.toGenericFailure())
                        is Error.Validation -> _registerAction.postValue(response.error.toValidationFailure())
                        is Error.NetWork -> _registerAction.postValue(response.error.toGenericFailure())
                    }
                }
                is Result.Success -> _registerAction.postValue(RequestAction.Success(response.value))
            }
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