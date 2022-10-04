package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.data.PasswordRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val repository: PasswordRepository,
) : BaseViewModel() {

    private val _resetAction: MutableLiveData<RequestAction> = MutableLiveData()
    val resetAction: LiveData<RequestAction> = _resetAction

    fun passwordReset(
        email: String,
        code: String,
        password: String,
        confirmPassword: String,
    ) {
        _resetAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val result = repository.passwordReset(
                email = email,
                code = code,
                password = password,
                confirmPassword = confirmPassword,
            )
            _resetAction.handleResponse(result)
        }
    }
}