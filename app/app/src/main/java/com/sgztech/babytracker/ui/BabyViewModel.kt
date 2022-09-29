package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.toGenericFailure
import com.sgztech.babytracker.arch.toValidationFailure
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.User
import kotlinx.coroutines.launch
import java.time.LocalDate

class BabyViewModel(
    preferenceService: PreferenceService,
    private val babyRepository: BabyRepository,
    private val formatter: DateTimeFormatter,
) : ViewModel() {

    private val _formState: MutableLiveData<BabyFormState> = MutableLiveData()
    val formState: LiveData<BabyFormState> = _formState

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    private val user: User = preferenceService.getUser()

    private val _baby: MutableLiveData<Baby?> = MutableLiveData()
    val baby: LiveData<Baby?> = _baby

    private val _saveAction: MutableLiveData<RequestAction> = MutableLiveData()
    val saveAction: LiveData<RequestAction> = _saveAction

    init {
        getBaby()
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun updateDate(date: LocalDate) {
        _date.postValue(date)
    }

    fun formatDate(date: LocalDate): String =
        formatter.format(date, "dd/MM/yyyy")

    fun saveBaby(baby: Baby) {
        viewModelScope.launch {
            _saveAction.postValue(RequestAction.Loading)
            val copyBaby = baby.copy(userId = user.id, webId = _baby.value?.webId)
            when (val response = babyRepository.save(copyBaby)) {
                is Result.Failure -> when (response.error) {
                    is Error.Unknown -> _saveAction.postValue(response.error.toGenericFailure())
                    is Error.Validation -> _saveAction.postValue(response.error.toValidationFailure())
                    is Error.NetWork -> _saveAction.postValue(response.error.toGenericFailure())
                }
                is Result.Success -> _saveAction.postValue(RequestAction.Success(response.value))
            }
        }
    }

    fun update(baby: Baby) {
        viewModelScope.launch {
            _saveAction.postValue(RequestAction.Loading)
            val copyBaby = baby.copy(id = _baby.value!!.id, userId = user.id, webId = _baby.value?.webId)
            when (val response = babyRepository.update(copyBaby)) {
                is Result.Failure -> when (response.error) {
                    is Error.Unknown -> _saveAction.postValue(response.error.toGenericFailure())
                    is Error.Validation -> _saveAction.postValue(response.error.toValidationFailure())
                    is Error.NetWork -> _saveAction.postValue(response.error.toGenericFailure())
                }
                is Result.Success -> _saveAction.postValue(RequestAction.Success(response.value))
            }
        }
    }

    private fun getBaby() =
        viewModelScope.launch {
            val baby = babyRepository.loadByUserId(user.id)
            _baby.postValue(baby)
            _date.postValue(baby?.birthday ?: LocalDate.now())
        }

    fun validate(name: String, sex: String) {
        if (name.isEmpty()) {
            _formState.postValue(BabyFormState.InvalidName(R.string.msg_enter_name))
            return
        }

        if (sex.isEmpty()) {
            _formState.postValue(BabyFormState.InvalidSex(R.string.msg_enter_sex))
            return
        }

        _formState.postValue(BabyFormState.Valid)
    }
}

sealed class BabyFormState {
    class InvalidName(val errorRes: Int) : BabyFormState()
    class InvalidSex(val errorRes: Int) : BabyFormState()
    object Valid : BabyFormState()
}
