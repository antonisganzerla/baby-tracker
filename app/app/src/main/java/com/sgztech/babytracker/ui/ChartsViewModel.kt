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

    private val _diaperRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val diaperRegisters: LiveData<List<Register>> = _diaperRegisters

    fun loadWeightRegisters() {
        _weightRegisters.loadRegistersByType(RegisterType.WEIGHT)
    }

    fun loadHeightRegisters() {
        _heightRegisters.loadRegistersByType(RegisterType.HEIGHT)
    }

    fun loadDiaperRegisters() {
        _diaperRegisters.loadRegistersByType(RegisterType.DIAPER)
    }

    private fun MutableLiveData<List<Register>>.loadRegistersByType(type: RegisterType) {
        viewModelScope.launch {
            val registers = repository.loadAllByUserIdAndType(
                userId = preferenceService.getUser().id,
                type = type,
            ).sortedBy { it.localDateTime }
            this@loadRegistersByType.postValue(registers)
        }
    }
}