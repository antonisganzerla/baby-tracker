package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChartsViewModel(
    private val preferenceService: PreferenceService,
    private val dateFormatter: DateTimeFormatter,
    private val repository: RegisterRepository,
) : ViewModel() {

    private val _weightRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val weightRegisters: LiveData<List<Register>> = _weightRegisters

    private val _heightRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val heightRegisters: LiveData<List<Register>> = _heightRegisters

    fun loadWeightRegisters() {
        viewModelScope.launch {
            val registers = repository.loadAllByUserIdAndType(
                userId = preferenceService.getUser().id,
                type = RegisterType.WEIGHT,
            ).sortedBy { it.localDateTime }
            _weightRegisters.postValue(registers)
        }
    }

    fun getWeightRegisterByIndex(index: Int): Register? =
        weightRegisters.value?.get(index)

    fun loadHeightRegisters() {
        viewModelScope.launch {
            val registers = repository.loadAllByUserIdAndType(
                userId = preferenceService.getUser().id,
                type = RegisterType.HEIGHT,
            ).sortedBy { it.localDateTime }
            _heightRegisters.postValue(registers)
        }
    }

    fun getHeightRegisterByIndex(index: Int): Register? =
        weightRegisters.value?.get(index)


    fun formatDate(date: LocalDateTime): String =
        dateFormatter.format(date.toLocalDate(), "dd/MM/yyyy")
}