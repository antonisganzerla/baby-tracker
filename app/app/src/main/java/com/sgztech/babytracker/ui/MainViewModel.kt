package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.arch.mapSuccess
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.User
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

class MainViewModel(
    private val dateFormatter: DateTimeFormatter,
    private val preferenceService: PreferenceService,
    private val repository: RegisterRepository,
    private val babyRepository: BabyRepository,
) : BaseViewModel() {

    private val _loadAction: MutableLiveData<RequestAction> = MutableLiveData()
    val loadAction: LiveData<RequestAction> = _loadAction

    private val _deleteAction: MutableLiveData<RequestAction> = MutableLiveData()
    val deleteAction: LiveData<RequestAction> = _deleteAction

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    private val user: User = preferenceService.getUser()

    private val _baby: MutableLiveData<Baby> = MutableLiveData()
    val baby: LiveData<Baby> = _baby

    init {
        viewModelScope.launch {
            val baby = babyRepository.loadByUserId(user.id)
            _baby.postValue(baby)
            _date.postValue(LocalDate.now())
        }
    }

    fun loadRegisters() {
        _loadAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val response = repository.load(
                userId = user.id,
                date = currentDate(),
            )
            _loadAction.handleResponse(response)
        }
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun addRegister(register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) {
        viewModelScope.launch {
            val result = repository.save(register.copy(userId = user.id)).mapSuccess { }
            handleResult(result)
        }
    }

    fun deleteRegister(register: Register) {
        _deleteAction.postValue(RequestAction.Loading)
        viewModelScope.launch {
            val result = repository.delete(register)
            _deleteAction.handleResponse(result)
        }
    }

    fun updateDate(date: LocalDate) {
        _date.postValue(date)
    }

    fun plusDay() {
        _date.postValue(date.value?.plusDays(1))
    }

    fun minusDay() {
        _date.postValue(date.value?.minusDays(1))
    }

    fun formatDate(date: LocalDate): String =
        dateFormatter.format(date)

    fun getPeriodBetween(): Period =
        Period.between(baby.value?.birthday, currentDate())

    fun getUser(): User = user

    fun logout() {
        preferenceService.setUserLogged(false)
    }
}