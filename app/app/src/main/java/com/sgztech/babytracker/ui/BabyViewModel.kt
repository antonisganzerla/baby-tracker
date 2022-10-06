package com.sgztech.babytracker.ui

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.BuildConfig
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.User
import com.sgztech.babytracker.service.ServiceFirebaseStorage
import kotlinx.coroutines.launch
import java.time.LocalDate

class BabyViewModel(
    preferenceService: PreferenceService,
    private val babyRepository: BabyRepository,
    private val formatter: DateTimeFormatter,
    private val firebaseStorage: ServiceFirebaseStorage,
) : BaseViewModel() {

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
            val response = babyRepository.save(copyBaby)
            if (response is Result.Success) {
                _baby.value = response.value
                update(response.value)
            } else
                _saveAction.handleResponse(response)
        }
    }

    fun update(baby: Baby) {
        viewModelScope.launch {
            _saveAction.postValue(RequestAction.Loading)
            val uploadResult = firebaseStorage.uploadPhoto(
                uriFile = baby.photoUri.toUri(),
                fileName = photoFileName(),
            )
            val result = when (uploadResult) {
                is Result.Failure -> {
                    val copyBaby = baby.copy(
                        id = _baby.value!!.id,
                        userId = user.id,
                        webId = _baby.value?.webId,
                    )
                    babyRepository.update(copyBaby)
                }
                is Result.Success -> {
                    val copyBaby = baby.copy(
                        id = _baby.value!!.id,
                        userId = user.id,
                        webId = _baby.value?.webId,
                        photoUri = uploadResult.value.toString(),
                    )
                    babyRepository.update(copyBaby)
                }
            }
            _saveAction.handleResponse(result)
        }
    }

    private fun photoFileName(): String =
        if (BuildConfig.DEBUG)
            _baby.value?.webId.toString().plus("dev")
        else
            _baby.value?.webId.toString()

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
