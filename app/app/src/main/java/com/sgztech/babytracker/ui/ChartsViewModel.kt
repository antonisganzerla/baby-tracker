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

class ChartsViewModel(
    private val preferenceService: PreferenceService,
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

    fun loadHeightRegisters() {
        viewModelScope.launch {
            val registers = repository.loadAllByUserIdAndType(
                userId = preferenceService.getUser().id,
                type = RegisterType.HEIGHT,
            ).sortedBy { it.localDateTime }
            _heightRegisters.postValue(registers)
        }
    }
}