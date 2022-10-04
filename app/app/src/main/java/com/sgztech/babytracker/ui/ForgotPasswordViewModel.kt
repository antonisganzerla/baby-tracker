package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.data.PasswordRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: PasswordRepository,
) : BaseViewModel() {

    private val _forgotAction: MutableLiveData<RequestAction> = MutableLiveData()
    val forgotAction: LiveData<RequestAction> = _forgotAction

    private val _codeAction: MutableLiveData<RequestAction> = MutableLiveData()
    val codeAction: LiveData<RequestAction> = _codeAction

    fun forgotPassword(email: String) {
        _forgotAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val result = repository.forgotPassword(email)
            _forgotAction.handleResponse(result)
        }
    }

    fun verifyCode(email: String, code: String) {
        _codeAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val result = repository.verifyCode(email, code)
            _codeAction.handleResponse(result)
        }
    }
}