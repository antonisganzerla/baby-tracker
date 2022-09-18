package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDate
import java.time.LocalDateTime

class MainViewModel : ViewModel() {

    private val datasource: MutableList<Register> = mutableListOf()

    private val _registers: MutableLiveData<List<Register>> = MutableLiveData()
    val registers: LiveData<List<Register>> = _registers

    private var _date: MutableLiveData<LocalDateTime> = MutableLiveData()
    val date: LiveData<LocalDateTime> = _date

    init {
        _date.postValue(LocalDateTime.now())
        datasource.addAll(fakeData())
    }

    fun loadRegisters() {
        val registers = datasource.filter { it.time.toLocalDate().isEqual(date.value?.toLocalDate() ?: LocalDate.now()) }
        _registers.postValue(registers)
    }

    private fun fakeData(): List<Register> = listOf(
        Register(
            icon = R.drawable.ic_bathtub_24,
            name = "Banho",
            description = "",
            time = LocalDateTime.now().plusHours(1),
        ),
        Register(
            icon = R.drawable.ic_food_bank_24,
            name = "Amamentação",
            description = "Esquerda 15:10",
            time = LocalDateTime.now().plusHours(2),
        ),
    )

    fun addRegister(register: Register) {
        datasource.add(register)
        loadRegisters()
    }

    fun updateDate(date: LocalDateTime?) {
        _date.postValue(date)
    }
}