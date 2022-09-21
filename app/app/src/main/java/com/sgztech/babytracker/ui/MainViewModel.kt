package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Register
import java.time.LocalDate

class MainViewModel(
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter(),
    private val repository: RegisterRepository = RegisterRepository(),
) : ViewModel() {

    private val _registers: MutableLiveData<List<Register>> = MutableLiveData()
    val registers: LiveData<List<Register>> = _registers

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    init {
        _date.postValue(LocalDate.now())
    }

    fun loadRegisters() {
        _registers.postValue(repository.load(currentDate()))
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun addRegister(register: Register) {
        repository.add(register)
        loadRegisters()
    }

    fun updateDate(date: LocalDate?) {
        _date.postValue(date)
    }

    fun plusDay() {
        updateDate(date.value?.plusDays(1))
    }

    fun minusDay() {
        updateDate(date.value?.minusDays(1))
    }

    fun formatDate(date: LocalDate): String =
        dateFormatter.format(date)
}