package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
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
) : ViewModel() {

    private val _registers: MutableLiveData<List<Register>> = MutableLiveData()
    val registers: LiveData<List<Register>> = _registers

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    private val user: User = preferenceService.getUser()

    init {
        _date.postValue(LocalDate.now())
    }

    fun loadRegisters() {
        viewModelScope.launch {
            _registers.postValue(
                repository.load(
                    userId = user.id,
                    date = currentDate(),
                )
            )
        }
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun addRegister(register: Register) {
        viewModelScope.launch {
            repository.add(register.copy(userId = user.id))
            loadRegisters()
        }
    }

    fun deleteRegister(register: Register) {
        viewModelScope.launch {
            repository.delete(register)
            loadRegisters()
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

    fun getBaby(): Baby =
        preferenceService.getBaby()

    fun getPeriodBetween(): Period =
        Period.between(getBaby().birthday, currentDate())

    fun getUser(): User = user

    fun logout() {
        preferenceService.setUserLogged(false)
    }
}